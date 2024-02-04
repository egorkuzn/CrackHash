package ru.nsu.fit.crackhash.worker.model.dto

data class CrackRequestDto (
    val hash: String,
    val maxLength: Int,
    val requestId: String
)