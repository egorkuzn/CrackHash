package ru.nsu.fit.crackhash.manager.service.impl

import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.dto.Status
import ru.nsu.fit.crackhash.manager.model.dto.WorkerResponseDto
import ru.nsu.fit.crackhash.manager.repo.ResponseDelayCountRepo
import ru.nsu.fit.crackhash.manager.repo.ResponseResultRepo
import java.time.LocalDateTime

@Service
class ResponseService(
    private val responseResultRepo: ResponseResultRepo,
    private val responseDelayCountRepo: ResponseDelayCountRepo,
) {
    /**
     * That method guarantee thread-safety.
     * Two atomic functions that can be swapped by JWM however it won't be critical for user.
     */
    fun putAll(response: WorkerResponseDto) {
        response.run {
            responseResultRepo.merge(requestId, value!!.toTypedArray())
            responseDelayCountRepo.merge(requestId, 1 to LocalDateTime.now())
        }
    }

    /**
     * All should be initialised.
     * Else you will get exception in putAll, when merge function want to make update by key.
     */
    fun prepareForResponse(it: String, size: Int) {
        responseDelayCountRepo[it] = size to LocalDateTime.now()
    }

    fun workerTimeout(requestId: String) {
        responseDelayCountRepo[requestId] = null to responseDelayCountRepo[requestId]!!.second
    }

    /**
     * Firstly, it should be checked: is it in queue.
     * And then take status of response.
     */
    fun responseStatus(requestId: String) = responseDelayCountRepo[requestId]!!.let {
        when (update(requestId).first) {
            null -> Status.ERROR
            0 -> Status.READY
            else -> Status.IN_PROGRESS
        }
    }

    private fun update(requestId: String): Pair<Int?, LocalDateTime> {
        responseDelayCountRepo.merge(requestId, 0 to LocalDateTime.now())
        return responseDelayCountRepo[requestId]!!
    }

    operator fun get(requestId: String) = responseResultRepo[requestId]
}