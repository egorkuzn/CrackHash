package ru.nsu.fit.crackhash.manager.repo

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

@Repository
class ResponseDelayCountRepo(
    @Value("\${workers.timeout}")
    private val timeout: Long,
) {
    /**
     * Keeps status of response.
     *
     * Key: String - requestId value
     *
     * Pair.first: Int - how many workers not finished. NULL mean timeout.
     *
     * Pair.second: LocalDateTime - keeps time of request start.
     */
    private val responseDelayCount = ConcurrentHashMap<String, Pair<Int?, LocalDateTime>>()


    private val mergeDelayFunction: (
        current: Pair<Int?, LocalDateTime>,
        newVal: Pair<Int?, LocalDateTime>,
    ) -> Pair<Int?, LocalDateTime> =
        { current, newVal ->
            when {
                isStable(current) -> current
                isTimeout(current, newVal) -> null to current.second
                else -> (current.first!! - newVal.first!!) to current.second
            }
        }

    private fun isStable(current: Pair<Int?, LocalDateTime>) = current.first == 0 || current.first == null

    private fun isTimeout(
        current: Pair<Int?, LocalDateTime>,
        newVal: Pair<Int?, LocalDateTime>,
    ) = Duration.between(current.second, newVal.second).toMinutes() >= timeout

    fun merge(requestId: String, value: Pair<Int?, LocalDateTime>) =
        responseDelayCount.merge(requestId, value, mergeDelayFunction)

    operator fun set(it: String, value: Pair<Int?, LocalDateTime>) { responseDelayCount[it] = value }

    operator fun get(requestId: String) = responseDelayCount[requestId]
}