package ru.nsu.fit.crackhash.manager.service.impl

import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.dto.CrackRequestDto
import ru.nsu.fit.crackhash.manager.model.dto.CrackResponseDto
import ru.nsu.fit.crackhash.manager.model.dto.Status
import ru.nsu.fit.crackhash.manager.model.dto.StatusResposeDto
import ru.nsu.fit.crackhash.manager.model.entity.CrackParam
import ru.nsu.fit.crackhash.manager.service.ManagerService
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

@Service
class ManagerServiceImpl: ManagerService {
    val requestRepo = ConcurrentHashMap<String, CrackParam>()
    val responseRepo = ConcurrentHashMap<String, Array<String>>()

    override fun crack(crackRequest: CrackRequestDto) = CrackResponseDto (
        requestId = randomString().let {
            requestRepo[it] = CrackParam(crackRequest)
            it
        }
    )

    private fun randomString() = String(Random.nextBytes(ByteArray(128)))

    override fun status(requestId: String) = requestId.let {
        if (responseRepo.containsKey(it))
            StatusResposeDto(
                Status.READY,
                responseRepo[it]
            )
        else
            StatusResposeDto (
                Status.IN_PROGRESS,
                null
            )
    }
}