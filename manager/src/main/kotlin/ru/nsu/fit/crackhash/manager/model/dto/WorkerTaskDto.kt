package ru.nsu.fit.crackhash.manager.model.dto

import ru.nsu.fit.crackhash.manager.model.entity.TaskMongoEntity

data class WorkerTaskDto (
    val hash: String,
    val maxLength: Int,
    val requestId: String,
    val partNumber: Int,
    val partCount: Int
) {
    constructor(task: TaskMongoEntity, partNumber: Int) : this(
        task.hash,
        task.maxLength,
        task.requestId,
        partNumber,
        task.partCount
    )
}