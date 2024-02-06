package ru.nsu.fit.crackhash.manager.repo

import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class ResponseRepo {

    private val responseRepo = ConcurrentHashMap<String, Array<String>>()
    private val responseDelayCount = ConcurrentHashMap<String, Int>()

    private val mergeFunction: (a: Array<String>, b: Array<String>) -> Array<String> = {
            a, b -> a + b
    }

    operator fun get(key: String) = responseRepo[key]

    fun putAll(response: Map<String, Array<String>>) {
        response.forEach {
            responseRepo.merge(it.key, it.value, mergeFunction)
            responseDelayCount.merge(it.key, 1, Int::minus)
        }
    }

    fun setDelayCountForResponse(it: String, size: Int) {
        responseDelayCount[it] = size
    }

    fun isFinished(requestId: String) = responseDelayCount[requestId] == 0
}