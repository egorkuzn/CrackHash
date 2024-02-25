package ru.nsu.fit.crackhash.worker.listener

import org.slf4j.Logger
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import ru.nsu.fit.crackhash.worker.model.dto.WorkerTaskDto

@Component
class TestListener(private val logger: Logger) {
    @RabbitListener(queues = ["test-queue"])
    fun testListener(dto: WorkerTaskDto) {
        logger.info(dto.requestId)
    }
}