package ru.nsu.fit.crackhash.worker.service.impl

import kotlinx.coroutines.*
import org.paukov.combinatorics3.Generator
import org.slf4j.Logger
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.DigestUtils
import ru.nsu.fit.crackhash.worker.model.dto.WorkerResponseDto
import ru.nsu.fit.crackhash.worker.model.enity.WorkerTask
import ru.nsu.fit.crackhash.worker.service.TaskExecutorService
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Service
class TaskExecutorServiceImpl(
    @Value("\${manager.timeout}")
    private val timeoutMinutes: Long,
    private val logger: Logger,
    private val manager: RabbitTemplate,
    @Value("\${worker.number}")
    private val workerNumber: Int,
) : TaskExecutorService {
    override fun takeNewTask(workerTask: WorkerTask): Unit = runBlocking {
        val loggerBase = workerTask.run { "Task [$partNumber|$partCount]#$requestId" }

        logger.info("$loggerBase: Started")

        withTimeoutOrNull(timeoutMinutes.toDuration(DurationUnit.MINUTES)) {
            executeTask(workerTask, loggerBase) { ensureActive() }
        }.let { result ->
            manager.convertAndSend(
                "worker-to-manager",
                "manager",
                WorkerResponseDto(
                    workerTask.partNumber,
                    workerTask.requestId,
                    result
                )
            )

            val nullState = if (result == null) "TIMEOUT" else "SUCCESS"
            logger.info("$loggerBase finished $nullState.")
        }
    }

    private fun executeTask(
        workerTask: WorkerTask,
        loggerBase: String,
        cancellationDispatch: () -> Unit,
    ): List<String> {
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