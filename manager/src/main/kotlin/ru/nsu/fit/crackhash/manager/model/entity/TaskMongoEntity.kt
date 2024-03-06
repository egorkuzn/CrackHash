package ru.nsu.fit.crackhash.manager.model.entity

import jakarta.validation.Constraint
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("worker_task")
data class TaskMongoEntity (
    @Id
    val requestId: String,
    val hash: String,
    val maxLength: Int,
    val partCount: Int,
    @Constraint(validatedBy = { partNumber < partCount })
    val partNumber: Int
)