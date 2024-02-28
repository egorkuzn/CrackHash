package ru.nsu.fit.crackhash.worker.service.impl

import kotlinx.coroutines.*
import kotlinx.coroutines.future.asCompletableFuture
import org.paukov.combinatorics3.Generator
import org.slf4j.Logger
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.DigestUtils
import ru.nsu.fit.crackhash.worker.model.dto.WorkerResponseDto
import ru.nsu.fit.crackhash.worker.model.enity.WorkerTask
import ru.nsu.fit.crackhash.worker.service.TaskExecutorService
import java.util.concurrent.TimeUnit

@Service
class TaskExecutorServiceImpl(
    @Value("\${manager.timeout}")
    private val timeoutMinutes: Long,
    private val logger: Logger,
    private val manager: RabbitTemplate
) : TaskExecutorService {
    val taskExecutorScope = CoroutineScope(Dispatchers.Default)

    override fun takeNewTask(workerTask: WorkerTask) {
        taskExecutorScope.launch {
            val loggerBase = workerTask.run { "Task [$partNumber|$partCount]#$requestId" }
            val job = async { executeTask(workerTask, loggerBase) { ensureActive() } }
            val res = job.asCompletableFuture()
                .completeOnTimeout(null, timeoutMinutes, TimeUnit.MINUTES)
                .get()

            manager.convertAndSend(
                "worker-to-manager",
                WorkerResponseDto(
                    workerTask.partNumber,
                    workerTask.requestId,
                    res
                )
            )

            job.cancelAndJoin()
            val crackResultStatus = if (res != null) "successfully" else "with timeout"
            logger.info("$loggerBase finished $crackResultStatus")
        }
    }

    private fun executeTask(workerTask: WorkerTask, loggerBase: String, cancellationDispatch: () -> Unit): List<String> {
        workerTask.apply { logger.info("$loggerBase started") }
        return (1..workerTask.maxLength).flatMap {
            workerTask.apply { logger.info("$loggerBase running $it/${workerTask.maxLength} symbols") }
            crackForFixedLength(it, workerTask, loggerBase, cancellationDispatch)
        }
    }

    private fun crackForFixedLength(
        length: Int,
        workerTask: WorkerTask,
        loggerBase: String,
        cancellationDispatch: () -> Unit,
    ): List<String> {
        var counter = -1

        return Generator.permutation(
            ('0'..'9') + ('a'..'z')
        ).withRepetitions(length)
            .stream()
            .peek { cancellationDispatch() }
            .skip(workerTask.partNumber.toLong())
            .peek { cancellationDispatch() }
            .filter { (++counter) % workerTask.partCount == 0 }
            .peek { cancellationDispatch() }
            .filter { hash(String(it.toCharArray())) == workerTask.hash }
            .peek { cancellationDispatch() }
            .map { String(it.toCharArray()) }
            .peek { workerTask.apply { logger.info("$loggerBase found '$it'") } }
            .distinct()
            .toList()
    }

    private fun hash(generation: String) = DigestUtils.md5DigestAsHex(generation.toByteArray())
}