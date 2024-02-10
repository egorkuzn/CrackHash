package ru.nsu.fit.crackhash.worker.service.impl

import org.slf4j.Logger
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.worker.model.dto.WorkerTaskDto
import ru.nsu.fit.crackhash.worker.model.enity.WorkerTask
import ru.nsu.fit.crackhash.worker.service.TaskExecutorService
import ru.nsu.fit.crackhash.worker.service.WorkerService

@Service
class WorkerServiceImpl(
    private val logger: Logger,
    private val taskExecutorService: TaskExecutorService
) : WorkerService {
    override fun takeTask(crackRequest: WorkerTaskDto) {
        crackRequest.apply { logger.info("Task [$partNumber|$partCount]#$requestId is taken") }
        taskExecutorService.takeNewTask(WorkerTask(crackRequest))
    }
}