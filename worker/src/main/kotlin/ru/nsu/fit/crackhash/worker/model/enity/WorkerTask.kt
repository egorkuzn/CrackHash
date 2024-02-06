package ru.nsu.fit.crackhash.worker.model.enity

import ru.nsu.fit.crackhash.worker.model.dto.CrackRequestDto

data class WorkerTask (
    val hash: String,
    val maxLength: Int,
    val requestId: String,
    val partNumber: Int,
    val partCount: Int
) {
    constructor(crackRequest: CrackRequestDto) : this(
        crackRequest.hash,
        crackRequest.maxLength,
        crackRequest.requestId,
        crackRequest.partCount,
        crackRequest.partCount
    )
}