package ru.nsu.fit.crackhash.manager.model.entity

import ru.nsu.fit.crackhash.manager.model.dto.CrackRequestDto

data class CrackParam (
    val hash: String,
    val maxLength: Int
) {
    constructor(crackRequestDto: CrackRequestDto): this(crackRequestDto.hash, crackRequestDto.maxLength)

}