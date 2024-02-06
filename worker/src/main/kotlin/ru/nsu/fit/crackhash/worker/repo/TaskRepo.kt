package ru.nsu.fit.crackhash.worker.repo

import org.springframework.stereotype.Repository
import ru.nsu.fit.crackhash.worker.model.enity.WorkerTask
import java.util.concurrent.ConcurrentLinkedDeque

@Repository
class TaskRepo {
    val taskRepo = ConcurrentLinkedDeque<Pair<String, WorkerTask>>()

    operator fun set(requestId: String, value: WorkerTask) {
        taskRepo.push(requestId to value)
    }

    fun takeFirstTask(): Pair<String, WorkerTask>? = taskRepo.first
}