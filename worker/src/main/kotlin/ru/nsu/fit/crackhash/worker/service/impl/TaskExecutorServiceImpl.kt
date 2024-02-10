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
            manager.sendTaskResult(
                WorkerResponseDto(
                    workerTask.requestId,
                    executeTask(workerTask)
                )
            )

            workerTask.apply { logger.info("Task [$partNumber|$partCount]#$requestId finished") }
        }
    }

    private fun executeTask(workerTask: WorkerTask): Array<String> {
        var counter = 0

        workerTask.apply { logger.info("Task [$partNumber|$partCount]#$requestId started") }
        return Generator.permutation(
            ('0'..'9') + ('a'..'z') + ('A'..'Z')
        ).withRepetitions(workerTask.maxLength)
            .stream()
            .skip(workerTask.partNumber.toLong())
            .filter { counter++ % workerTask.partCount == 0 }
            .filter { hash(String(it.toCharArray())) == workerTask.hash }
            .map { String(it.toCharArray()) }
            .peek { workerTask.apply { logger.info("Task [$partNumber|$partCount]#$requestId found $it") } }
            .toList().filterNotNull().toTypedArray()
    }

    private fun hash(generation: String) = DigestUtils.md5DigestAsHex(generation.toByteArray())
}