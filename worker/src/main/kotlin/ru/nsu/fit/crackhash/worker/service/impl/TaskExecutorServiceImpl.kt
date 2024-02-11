package ru.nsu.fit.crackhash.worker.service.impl

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import org.paukov.combinatorics3.Generator
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.DigestUtils
import ru.nsu.fit.crackhash.worker.manager.ManagerApi
import ru.nsu.fit.crackhash.worker.model.dto.WorkerResponseDto
import ru.nsu.fit.crackhash.worker.model.enity.WorkerTask
import ru.nsu.fit.crackhash.worker.service.TaskExecutorService
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Service
class TaskExecutorServiceImpl(
    @Value("\${timeout}")
    private val timeoutMinutes: Long,
    private val logger: Logger,
    private val manager: ManagerApi,
) : TaskExecutorService {
    @OptIn(DelicateCoroutinesApi::class)
    override fun takeNewTask(workerTask: WorkerTask) {
        GlobalScope.launch {
            val loggerBase = workerTask.run { "Task [$partNumber|$partCount]#$requestId" }
            val res = withTimeoutOrNull(timeoutMinutes.millis()) {
                executeTask(workerTask, loggerBase)
            }

            manager.sendTaskResult(
                WorkerResponseDto(
                    workerTask.partNumber,
                    workerTask.requestId,
                    res
                )
            )

            val crackResultStatus = if (res != null) "successfully" else "with timeout"

            logger.info("$loggerBase finished $crackResultStatus")
        }
    }

    private fun Long.millis() = this.toDuration(DurationUnit.MINUTES).inWholeMilliseconds

    private fun executeTask(workerTask: WorkerTask, loggerBase: String): List<String> {
        workerTask.apply { logger.info("$loggerBase started") }

        return (1..workerTask.maxLength).flatMap {
            workerTask.apply { logger.info("$loggerBase running $it/${workerTask.maxLength} symbols") }
            crackForFixedLength(it, workerTask, loggerBase)
        }
    }

    private fun crackForFixedLength(
        length: Int,
        workerTask: WorkerTask,
        loggerBase: String,
    ): List<String> {
        var counter = -1

        return Generator.permutation(
            ('0'..'9') + ('a'..'z')
        ).withRepetitions(length)
            .stream()
            .skip(workerTask.partNumber.toLong())
            .filter { (++counter) % workerTask.partCount == 0 }
            .filter { hash(String(it.toCharArray())) == workerTask.hash }
            .map { String(it.toCharArray()) }
            .peek { workerTask.apply { logger.info("$loggerBase found '$it'") } }
            .distinct()
            .toList()
    }

    private fun hash(generation: String) = DigestUtils.md5DigestAsHex(generation.toByteArray())
}