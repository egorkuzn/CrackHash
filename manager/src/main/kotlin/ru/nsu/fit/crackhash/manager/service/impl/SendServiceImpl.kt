package ru.nsu.fit.crackhash.manager.service.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.dto.WorkerTaskDto
import ru.nsu.fit.crackhash.manager.repo.TaskRepo
import ru.nsu.fit.crackhash.manager.service.SendService
import ru.nsu.fit.crackhash.manager.service.WorkerIterableService

@Service
class SendServiceImpl(
    @Value("\${workers.count}")
    private val partCount: Int,
    private val workers: WorkerIterableService,
    private val taskRepo: TaskRepo,
    private val logger: Logger,
) : SendService {
    var sendServiceCoroutineScope = CoroutineScope(Dispatchers.Default)

    override fun execute() {
        sendServiceCoroutineScope.launch {
            val task = taskRepo.takeTask()

            workers.forEach { worker ->
                worker.run {
                    launch {
                        logger.info("Sending [$partNumber|$partCount] ${task.requestId}")

                        client.takeTask(
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
    }
}