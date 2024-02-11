package ru.nsu.fit.crackhash.worker.model.dto

data class WorkerResponseDto(
    val partNumber: Int,
    val requestId: String,
    val value: List<String>?
)