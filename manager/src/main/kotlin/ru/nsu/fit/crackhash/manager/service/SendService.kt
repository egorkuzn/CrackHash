package ru.nsu.fit.crackhash.manager.service

import ru.nsu.fit.crackhash.manager.model.entity.TaskMongoEntity

interface SendService {
    fun execute(requestId: String)
    fun sendAfterRabbitReconnect(findAllByTaskStatus: List<TaskMongoEntity>)
}