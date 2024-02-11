package ru.nsu.fit.crackhash.manager.repo

import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class ResponseResultRepo {
    private val responseResultRepo = ConcurrentHashMap<String, Array<String>>()

    /**
     * That method adds MD5 hash proto and remove duplicates.
     */
    private val mergeResponseFunction: (
        current: Array<String>,
        newVal: Array<String>,
    ) -> Array<String> = { current, newVal -> (current + newVal).distinct().toTypedArray() }

    operator fun get(key: String) = responseResultRepo[key]

    operator fun set(key: String, value: Array<String>) { responseResultRepo[key] = value}

    fun merge(requestId: String, value: Array<String>) = responseResultRepo.merge(requestId, value, mergeResponseFunction)
}