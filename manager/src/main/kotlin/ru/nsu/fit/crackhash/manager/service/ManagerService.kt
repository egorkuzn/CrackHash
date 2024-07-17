package ru.nsu.fit.crackhash.manager.service

import ru.nsu.fit.crackhash.manager.model.dto.CrackRequestDto
import ru.nsu.fit.crackhash.manager.model.dto.CrackResponseDto
import ru.nsu.fit.crackhash.manager.model.dto.StatusResponseDto


interface ManagerService {
    fun crack(crackRequest: CrackRequestDto): CrackResponseDto
    fun status(requestId: String): StatusResponseDto
}