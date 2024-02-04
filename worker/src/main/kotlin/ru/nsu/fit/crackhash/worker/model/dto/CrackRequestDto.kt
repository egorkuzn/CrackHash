package ru.nsu.fit.crackhash.worker.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class CrackRequestDto(
    val hash: String,
    val maxLength: Int,
    @JsonProperty("request_id")
    val requestId: String,
)