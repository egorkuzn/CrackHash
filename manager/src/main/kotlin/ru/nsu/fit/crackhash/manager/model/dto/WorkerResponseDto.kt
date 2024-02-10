package ru.nsu.fit.crackhash.manager.model.dto

data class WorkerResponseDto(
    val partNumber: Int,
    val responseId: String,
    val value: List<String>
)
