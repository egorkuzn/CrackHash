package ru.nsu.fit.crackhash.manager.model.dto

data class CrackRequestDto (
    val hash: String,
    val maxLength: Int
)