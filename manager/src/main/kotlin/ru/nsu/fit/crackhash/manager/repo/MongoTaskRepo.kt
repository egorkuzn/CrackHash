package ru.nsu.fit.crackhash.manager.repo

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.nsu.fit.crackhash.manager.model.entity.TaskMongoEntity
import ru.nsu.fit.crackhash.manager.model.entity.TaskStatus

@Repository
interface MongoTaskRepo: MongoRepository<TaskMongoEntity, String> {
    /**
     * Нужен для обновления статуса
     */
    fun updateStatus(requestId: String, status: TaskStatus)
}