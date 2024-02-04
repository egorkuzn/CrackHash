package ru.nsu.fit.crackhash.manager.repo

import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class ResponseRepo {
    private val responseRepo = ConcurrentHashMap<String, Array<String>>()

    fun containsKey(key: String) = responseRepo.containsKey(key)

    operator fun get(key: String) = responseRepo[key]

    fun putAll(response: Map<String, Array<String>>) = responseRepo.putAll(response)
}