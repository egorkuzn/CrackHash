package ru.nsu.fit.crackhash.manager.model.dto

data class StatusResposeDto (
    val status: Status,
    val data: Array<String>?
)