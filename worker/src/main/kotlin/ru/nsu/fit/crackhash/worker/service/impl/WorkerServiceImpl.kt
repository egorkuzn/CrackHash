package ru.nsu.fit.crackhash.worker.service.impl

import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.worker.model.dto.CrackRequestDto
import ru.nsu.fit.crackhash.worker.model.enity.WorkerTask
import ru.nsu.fit.crackhash.worker.service.WorkerService
import java.util.concurrent.ConcurrentHashMap

@Service
class WorkerServiceImpl : WorkerService {
    val taskRepo = ConcurrentHashMap<String, WorkerTask>()
    val resultRepo = ConcurrentHashMap<String, Array<String>>()

    override fun takeTask(crackRequest: CrackRequestDto) {
        taskRepo[crackRequest.requestId] = WorkerTask(crackRequest)
    }

    override fun getTasksResults(): Map<String, Array<String>> {
        val result = HashMap(resultRepo)
        resultRepo.clear()
        return result
    }
}