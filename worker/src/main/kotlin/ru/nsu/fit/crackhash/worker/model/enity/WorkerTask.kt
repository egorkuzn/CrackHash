package ru.nsu.fit.crackhash.worker.model.enity

import ru.nsu.fit.crackhash.worker.model.dto.WorkerTaskDto

data class WorkerTask (
    val hash: String,
    val maxLength: Int,
    val requestId: String,
    val partNumber: Int,
    val partCount: Int
) {
    constructor(crackRequest: WorkerTaskDto) : this(
        crackRequest.hash,
        crackRequest.maxLength,
        crackRequest.requestId,
        crackRequest.partNumber,
        crackRequest.partCount
    )
}