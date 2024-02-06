package ru.nsu.fit.crackhash.manager.repo

import org.springframework.stereotype.Repository
import ru.nsu.fit.crackhash.manager.model.entity.CrackParam
import java.util.concurrent.ConcurrentHashMap

@Repository
class TaskRepo {
    private val requestRepo = ConcurrentHashMap<String, CrackParam>()

    operator fun set(it: String, value: CrackParam) {
        requestRepo[it] = value
    }

    fun takeDerivedTask() {

    }
}