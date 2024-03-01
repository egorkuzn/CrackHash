package ru.nsu.fit.crackhash.worker.listener

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.nsu.fit.crackhash.worker.model.dto.WorkerTaskDto
import ru.nsu.fit.crackhash.worker.service.WorkerService

@Component
class WorkerListener(
    private val workerService: WorkerService,
    @Value("\${worker.number}") private val workerNumber: String
) {
    @RabbitListener(queues = ["manager-to-worker"])
    fun taskListener(crackRequest: WorkerTaskDto) = workerService.takeTask(crackRequest)

    @RabbitListener(queues = ["queue-dlx-$workerNumber"])
    fun deadLetterExchangeQueueListener(deadResponse: )
}