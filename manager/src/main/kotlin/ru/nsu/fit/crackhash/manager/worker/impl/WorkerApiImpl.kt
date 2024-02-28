package ru.nsu.fit.crackhash.manager.worker.impl

import org.springframework.amqp.core.AmqpTemplate
import org.springframework.amqp.core.Queue
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.dto.WorkerTaskDto
import ru.nsu.fit.crackhash.manager.worker.WorkerApi

@Service
class WorkerApiImpl(
    private val template: AmqpTemplate,
    private val queue: Queue
): WorkerApi {

    override suspend fun takeTask(crackRequest: WorkerTaskDto) {
        template
    }
}