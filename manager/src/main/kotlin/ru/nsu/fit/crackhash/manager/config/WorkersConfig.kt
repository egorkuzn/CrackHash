package ru.nsu.fit.crackhash.manager.config

import org.slf4j.Logger
import org.springframework.amqp.AmqpException
import org.springframework.amqp.rabbit.connection.ConnectionListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.nsu.fit.crackhash.manager.model.dto.WorkerTaskDto
import ru.nsu.fit.crackhash.manager.model.entity.WorkerEntity
import ru.nsu.fit.crackhash.manager.repo.MongoTaskRepo
import ru.nsu.fit.crackhash.manager.worker.WorkerApi

@Configuration
class WorkersConfig(
    private val logger: Logger,
    private val mongoRepo: MongoTaskRepo,
    @Value("\${workers.count}") private val workersCount: Int
) {
    @Bean
    fun connectionListener() = ConnectionListener {
        logger.info("Rabbit reconnect")

    }

    @Bean
    fun workerApi(template: RabbitTemplate) = object : WorkerApi {
        override fun takeTask(task: WorkerTaskDto) {
            try {
                template.convertAndSend(
                    "manager-to-worker",
                    "worker",
                    task
                )

            } catch (e: AmqpException) {
                logger.error(
                    """
                    Got AmqpException ${e::class.simpleName}.
                    Task will be saved in data base"
                    """
                )
            }
        }
    }

    @Bean
    fun workers(
        workerApi: WorkerApi,
        template: RabbitTemplate,
        connectionListener: ConnectionListener
    ): List<WorkerEntity> {
        template.connectionFactory.addConnectionListener(connectionListener)
        return (1..workersCount).map { partNumber -> WorkerEntity(workerApi, partNumber) }
    }
}