package ru.nsu.fit.crackhash.worker.model.dto

data class WorkerTaskDto(
    val hash: String,
    val maxLength: Int,
    val requestId: String,
    val partNumber: Int,
    val partCount: Int
)