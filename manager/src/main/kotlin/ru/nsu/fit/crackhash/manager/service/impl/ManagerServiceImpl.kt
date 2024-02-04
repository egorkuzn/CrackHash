package ru.nsu.fit.crackhash.manager.service.impl

import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.dto.CrackRequestDto
import ru.nsu.fit.crackhash.manager.model.dto.CrackResponseDto
import ru.nsu.fit.crackhash.manager.model.dto.Status
import ru.nsu.fit.crackhash.manager.model.dto.StatusResposeDto
import ru.nsu.fit.crackhash.manager.model.entity.CrackParam
import ru.nsu.fit.crackhash.manager.repo.RequestRepo
import ru.nsu.fit.crackhash.manager.repo.ResponseRepo
import ru.nsu.fit.crackhash.manager.service.ManagerService
import kotlin.random.Random

@Service
class ManagerServiceImpl(
    private val requestRepo: RequestRepo,
    private val responseRepo: ResponseRepo,
) : ManagerService {
    override fun crack(crackRequest: CrackRequestDto) = CrackResponseDto(
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
            StatusResposeDto(
                Status.IN_PROGRESS,
                null
            )
    }
}