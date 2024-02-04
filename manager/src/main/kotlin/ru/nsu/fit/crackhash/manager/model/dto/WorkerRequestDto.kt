package ru.nsu.fit.crackhash.manager.model.dto

data class WorkerRequestDto (
    val hash: String,
    val maxLength: Int,
    val requestId: String
)