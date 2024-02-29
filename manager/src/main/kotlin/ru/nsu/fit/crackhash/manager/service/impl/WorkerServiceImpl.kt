package ru.nsu.fit.crackhash.manager.service.impl

import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.dto.WorkerTaskDto
import ru.nsu.fit.crackhash.manager.model.entity.WorkerEntity
import ru.nsu.fit.crackhash.manager.service.WorkerService
import ru.nsu.fit.crackhash.manager.worker.WorkerApi

@Service
class WorkerServiceImpl(
    template: RabbitTemplate,
    @Qualifier("directExchangeManagerToWorker") exchange: DirectExchange,
    @Value("\${workers.count}") private val workersCount: Int,
): WorkerService {
    val workers = (1..workersCount).map {
        WorkerEntity(
            object: WorkerApi {
                override fun takeTask(task: WorkerTaskDto) {
                    template.convertAndSend(
                        exchange.name,
                        it.toString(),
                        task
                    )
                }
            },
            it
        )
    }

    override fun iterator() = workers.iterator()
}