package ru.nsu.fit.crackhash.manager.repo

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ru.nsu.fit.crackhash.manager.model.entity.TaskMongoEntity
import ru.nsu.fit.crackhash.manager.model.entity.TaskStatus

@Repository
interface MongoTaskRepo : MongoRepository<TaskMongoEntity, String> {
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun findFirstByRequestId(requestId: String): TaskMongoEntity

    fun findAllByTaskStatus(status: TaskStatus): List<TaskMongoEntity>
}