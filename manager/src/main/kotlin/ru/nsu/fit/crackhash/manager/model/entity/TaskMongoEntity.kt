package ru.nsu.fit.crackhash.manager.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("worker_task")
data class TaskMongoEntity (
    @Id
    private val requestId: String,
    private val hash: String,
    private val maxLength: Int,
    private val partNumber: Int
)