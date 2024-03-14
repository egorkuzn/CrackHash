package ru.nsu.fit.crackhash.manager.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("worker_task")
data class TaskMongoEntity(
    @Id
    val requestId: String,
    val hash: String,
    val maxLength: Int,
    val partCount: Int,
    val partNumber: Int,
    val time: LocalDateTime = LocalDateTime.now()
)