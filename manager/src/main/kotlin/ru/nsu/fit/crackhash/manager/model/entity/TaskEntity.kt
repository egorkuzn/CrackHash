package ru.nsu.fit.crackhash.manager.model.entity

data class TaskEntity(
    val requestId: String,
    val hash: String,
    val maxLength: Int,
) {
    constructor(requestId: String, crackParam: CrackParam) : this(
        requestId,
        crackParam.hash,
        crackParam.maxLength
    )
}