package ru.nsu.fit.crackhash.manager.service.impl

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.dto.WorkerTaskDto
import ru.nsu.fit.crackhash.manager.model.entity.WorkerEntity
import ru.nsu.fit.crackhash.manager.repo.TaskRepo
import ru.nsu.fit.crackhash.manager.service.SendService

@Service
class SendServiceImpl(
    @Value("\${workers.count}")
    private val partCount: Int,
    private val workers: List<WorkerEntity>,
    private val taskRepo: TaskRepo,
    private val logger: Logger,
) : SendService {
    @OptIn(DelicateCoroutinesApi::class)
    override fun execute() {
        GlobalScope.launch {
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