package ru.nsu.fit.crackhash.worker.service.impl

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.paukov.combinatorics3.Generator
import org.slf4j.Logger
import org.springframework.stereotype.Service
import org.springframework.util.DigestUtils
import ru.nsu.fit.crackhash.worker.manager.ManagerApi
import ru.nsu.fit.crackhash.worker.model.dto.WorkerResponseDto
import ru.nsu.fit.crackhash.worker.model.enity.WorkerTask
import ru.nsu.fit.crackhash.worker.service.TaskExecutorService

@Service
class TaskExecutorServiceImpl(
    private val logger: Logger,
    private val manager: ManagerApi,
) : TaskExecutorService {
    @OptIn(DelicateCoroutinesApi::class)
    override fun takeNewTask(workerTask: WorkerTask) {
        GlobalScope.launch {
            val loggerBase = workerTask.run { "Task [$partNumber|$partCount]#$requestId" }

            manager.sendTaskResult(
                WorkerResponseDto(
                    workerTask.partNumber,
                    workerTask.requestId,
                    executeTask(workerTask, loggerBase)
                )
            )

            logger.info("$loggerBase finished")
        }
    }

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
        var counter = 0

        return Generator.permutation(
            ('0'..'9') + ('a'..'z') + ('A'..'Z')
        ).withRepetitions(length)
            .stream()
            .skip(workerTask.partNumber.toLong())
            .filter { counter++ % workerTask.partCount == 0 }
            .filter { hash(String(it.toCharArray())) == workerTask.hash }
            .map { String(it.toCharArray()) }
            .peek { workerTask.apply { logger.info("$loggerBase found '$it'") } }
            .distinct()
            .toList()
    }

    private fun hash(generation: String) = DigestUtils.md5DigestAsHex(generation.toByteArray())
}