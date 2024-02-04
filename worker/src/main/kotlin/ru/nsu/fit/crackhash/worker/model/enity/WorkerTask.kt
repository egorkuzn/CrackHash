package ru.nsu.fit.crackhash.worker.model.enity

import ru.nsu.fit.crackhash.worker.model.dto.CrackRequestDto

data class WorkerTask (
    val hash: String,
    val maxLength: Int
) {
    constructor(crackRequest: CrackRequestDto) : this(crackRequest.hash, crackRequest.maxLength)
}