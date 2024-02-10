package ru.nsu.fit.crackhash.manager.service.impl

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.dto.WorkerTaskDto
import ru.nsu.fit.crackhash.manager.repo.TaskRepo
import ru.nsu.fit.crackhash.manager.service.SendService
import ru.nsu.fit.crackhash.manager.worker.WorkerApi

@Service
class SendServiceImpl(
    @Value("\${workers.count}")
    private val partCount: Int,
    private val worker: WorkerApi,
    private val taskRepo: TaskRepo,
    private val logger: Logger
) : SendService {
    override fun execute() = runBlocking {
        val task = taskRepo.takeTask()

        (1..partCount).forEach { partNumber ->
            launch {
                logger.info("Sending [$partNumber|$partCount] ${task.requestId}")
                worker.takeTask(
                    WorkerTaskDto(
                        task.hash,
                        task.maxLength,
                        task.requestId,
                        partNumber,
                        partCount
                    )
                )
                logger.info("Sent [$partNumber|$partCount] ${task.requestId}")
            }
        }
    }
}