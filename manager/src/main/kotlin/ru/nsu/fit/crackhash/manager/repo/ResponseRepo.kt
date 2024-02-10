package ru.nsu.fit.crackhash.manager.repo

import org.slf4j.Logger
import org.springframework.stereotype.Repository
import ru.nsu.fit.crackhash.manager.model.dto.WorkerResponseDto
import java.util.concurrent.ConcurrentHashMap

@Repository
class ResponseRepo(private val logger: Logger) {

    private val responseRepo = ConcurrentHashMap<String, Array<String>>()
    private val responseDelayCount = ConcurrentHashMap<String, Int>()

    private val mergeFunction: (a: Array<String>, b: Array<String>) -> Array<String> = { a, b ->
        a + b
    }

    operator fun get(key: String) = responseRepo[key]

    fun putAll(response: WorkerResponseDto) {
        response.run {
            if (value != null && value.isNotEmpty())
                responseRepo.merge(responseId, value, mergeFunction)

            responseDelayCount.merge(responseId, 1, Int::minus)
        }
    }

    fun setDelayCountForResponse(it: String, size: Int) {
        responseRepo[it] = arrayOf("")
        responseDelayCount[it] = size
    }

    fun isFinished(requestId: String) = responseDelayCount[requestId] == 0
}