package ru.nsu.fit.crackhash.manager.worker.impl

import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.springframework.amqp.AmqpException
import org.springframework.amqp.rabbit.connection.ConnectionListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.dto.WorkerTaskDto
import ru.nsu.fit.crackhash.manager.model.entity.TaskMongoEntity
import ru.nsu.fit.crackhash.manager.repo.MongoTaskRepo
import ru.nsu.fit.crackhash.manager.worker.WorkerApi

@Service
class WorkerApiImpl(
    private val logger: Logger,
    private val template: RabbitTemplate,
    private val mongoTaskRepo: MongoTaskRepo,
    private val connectionListener: ConnectionListener
): WorkerApi {
    @PostConstruct
    override fun init() {
        template.connectionFactory.addConnectionListener(connectionListener)
    }

    override fun takeTask(task: TaskMongoEntity, partNumber: Int) {
        try {
            mongoTaskRepo.save(task.apply { sendSet = sendSet.filterNot { it == partNumber }.toSet() })
            template.convertAndSend(
                "manager-to-worker",
                "worker",
                WorkerTaskDto(task, partNumber)
            )
        } catch (e: AmqpException) {
            mongoTaskRepo.save(task.apply { sendSet = sendSet + partNumber })
            logger.error(
                """
                    Got AmqpException ${e::class.simpleName}.
                    Task will be saved in data base
                """
            )
        }
    }
}