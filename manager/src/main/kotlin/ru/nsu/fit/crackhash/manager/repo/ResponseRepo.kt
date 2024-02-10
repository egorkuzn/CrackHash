package ru.nsu.fit.crackhash.manager.repo

import org.springframework.stereotype.Repository
import ru.nsu.fit.crackhash.manager.model.dto.WorkerResponseDto
import java.util.concurrent.ConcurrentHashMap

@Repository
class ResponseRepo {

    private val responseRepo = ConcurrentHashMap<String, Array<String>>()
    private val responseDelayCount = ConcurrentHashMap<String, Int>()

    /**
     * That method adds MD5 hash proto and remove duplicates.
     */
    private val mergeFunction: (a: Array<String>, b: Array<String>) -> Array<String> = { a, b ->
        (a + b).distinct().toTypedArray()
    }

    operator fun get(key: String) = responseRepo[key]

    /**
     * That method guarantee thread-safety.
     * Two atomic functions that can be swapped by JWM however it won't be critical for user.
     */
    fun putAll(response: WorkerResponseDto) {
        response.run {
            responseRepo.merge(responseId, value.toTypedArray(), mergeFunction)
            responseDelayCount.merge(responseId, 1, Int::minus)
        }
    }

    /**
     * All should be initialised.
     * Else you will get exception in putAll, when merge function want to make update by key.
     */
    fun prepareForResponse(it: String, size: Int) {
        responseRepo[it] = emptyArray()
        responseDelayCount[it] = size
    }

    fun isFinished(requestId: String) = responseDelayCount[requestId] == 0
}