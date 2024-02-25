package ru.nsu.fit.crackhash.manager.model.dto

data class WorkerTaskDto (
    val hash: String,
    val maxLength: Int,
    val requestId: String,
    val partNumber: Int,
    val partCount: Int
)