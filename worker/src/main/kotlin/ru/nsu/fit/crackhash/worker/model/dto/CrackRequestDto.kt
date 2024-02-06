package ru.nsu.fit.crackhash.worker.model.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@Suppress("DEPRECATION")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class CrackRequestDto (
    val hash: String,
    val maxLength: Int,
    val requestId: String,
    val partNumber: Int,
    val partCount: Int
)