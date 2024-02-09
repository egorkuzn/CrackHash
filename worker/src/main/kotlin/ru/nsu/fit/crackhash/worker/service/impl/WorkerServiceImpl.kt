package ru.nsu.fit.crackhash.worker.service.impl

import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.worker.model.dto.CrackRequestDto
import ru.nsu.fit.crackhash.worker.model.enity.WorkerTask
import ru.nsu.fit.crackhash.worker.service.TaskExecutorService
import ru.nsu.fit.crackhash.worker.service.WorkerService
import java.util.concurrent.ConcurrentHashMap

@Service
class WorkerServiceImpl(private val taskExecutorService: TaskExecutorService) : WorkerService {
    override fun takeTask(crackRequest: CrackRequestDto) {
        taskExecutorService.takeNewTask(WorkerTask(crackRequest))
    }
}