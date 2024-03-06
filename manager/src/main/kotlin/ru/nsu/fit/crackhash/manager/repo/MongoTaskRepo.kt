package ru.nsu.fit.crackhash.manager.repo

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.nsu.fit.crackhash.manager.model.entity.TaskMongoEntity

@Repository
interface MongoTaskRepo: MongoRepository<TaskMongoEntity, String>