package ru.nsu.fit.crackhash.manager.repo

import org.springframework.stereotype.Repository
import ru.nsu.fit.crackhash.manager.model.entity.CrackParam
import ru.nsu.fit.crackhash.manager.model.entity.TaskEntity
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque

@Repository
class TaskRepo {
    private val requestIds = ConcurrentLinkedDeque<String>()
    private val requestRepo = ConcurrentHashMap<String, CrackParam>()

    operator fun set(requestId: String, value: CrackParam) {
        requestRepo[requestId] = value
        requestIds.push(requestId)
    }

    fun takeTask() = requestIds.poll()
        .let { TaskEntity(it, requestRepo[it]!!) }
}