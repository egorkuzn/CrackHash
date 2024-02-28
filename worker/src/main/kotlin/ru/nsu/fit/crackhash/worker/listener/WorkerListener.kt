package ru.nsu.fit.crackhash.worker.listener

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import ru.nsu.fit.crackhash.worker.model.dto.WorkerTaskDto
import ru.nsu.fit.crackhash.worker.service.WorkerService

@Component
class WorkerListener(private val workerService: WorkerService) {
    @RabbitListener(queues = ["test-queue"])
    fun taskListener(crackRequest: WorkerTaskDto) = workerService.takeTask(crackRequest)
}