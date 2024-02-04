package ru.nsu.fit.crackhash.manager.service.impl

import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.dto.CrackRequestDto
import ru.nsu.fit.crackhash.manager.model.dto.CrackResponseDto
import ru.nsu.fit.crackhash.manager.model.dto.StatusResposeDto
import ru.nsu.fit.crackhash.manager.service.ManagerService

@Service
class ManagerServiceImpl: ManagerService {
    override fun crack(crackRequest: CrackRequestDto): CrackResponseDto {
        TODO("Not yet implemented")
    }

    override fun status(requestId: String): StatusResposeDto {
        TODO("Not yet implemented")
    }
}