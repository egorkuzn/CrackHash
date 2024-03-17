package ru.nsu.fit.crackhash.manager.model.dto

import ru.nsu.fit.crackhash.manager.model.entity.TaskStatus

data class StatusResposeDto (
    val status: TaskStatus,
    val data: Array<String>?
)