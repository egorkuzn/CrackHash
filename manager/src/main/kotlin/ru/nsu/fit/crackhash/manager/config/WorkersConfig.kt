package ru.nsu.fit.crackhash.manager.config

import org.slf4j.Logger
import org.springframework.amqp.rabbit.connection.ConnectionListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.nsu.fit.crackhash.manager.model.entity.TaskStatus
import ru.nsu.fit.crackhash.manager.repo.MongoTaskRepo
import ru.nsu.fit.crackhash.manager.service.SendService

@Configuration
class WorkersConfig(
    private val logger: Logger,
    private val mongoRepo: MongoTaskRepo,
    private val sendService: SendService
) {
    @Bean
    fun connectionListener() = ConnectionListener {
        logger.info("Rabbit reconnect")
        sendService.sendAfterRabbitReconnect(
            mongoRepo.findAllByTaskStatus(TaskStatus.IN_PROGRESS)
        )
    }
}