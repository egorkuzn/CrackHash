package ru.nsu.fit.crackhash.manager.model.dto

import ru.nsu.fit.crackhash.manager.model.entity.TaskStatus

data class StatusResponseDto (
    val status: TaskStatus,
    val data: Array<String>?
)