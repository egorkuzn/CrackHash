package ru.nsu.fit.crackhash.worker.service.impl

import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.worker.model.dto.CrackRequestDto
import ru.nsu.fit.crackhash.worker.model.enity.WorkerTask
import ru.nsu.fit.crackhash.worker.repo.TaskRepo
import ru.nsu.fit.crackhash.worker.service.WorkerService
import java.util.concurrent.ConcurrentHashMap

@Service
class WorkerServiceImpl(private val taskRepo: TaskRepo) : WorkerService {
    override fun takeTask(crackRequest: CrackRequestDto) {
        taskRepo[crackRequest.requestId] = WorkerTask(crackRequest)
    }
}