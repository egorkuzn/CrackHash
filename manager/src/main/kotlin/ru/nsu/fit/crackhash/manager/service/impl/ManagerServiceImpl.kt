package ru.nsu.fit.crackhash.manager.service.impl

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.dto.CrackRequestDto
import ru.nsu.fit.crackhash.manager.model.dto.CrackResponseDto
import ru.nsu.fit.crackhash.manager.model.dto.Status
import ru.nsu.fit.crackhash.manager.model.dto.StatusResposeDto
import ru.nsu.fit.crackhash.manager.model.entity.CrackParam
import ru.nsu.fit.crackhash.manager.repo.ResponseRepo
import ru.nsu.fit.crackhash.manager.repo.TaskRepo
import ru.nsu.fit.crackhash.manager.service.ManagerService
import ru.nsu.fit.crackhash.manager.service.SendService
import java.util.*

@Service
class ManagerServiceImpl(
    @Value("\${workers.count}")
    private val partCount: Int,
    private val taskRepo: TaskRepo,
    private val sendService: SendService,
    private val responseRepo: ResponseRepo,
) : ManagerService {
    @OptIn(DelicateCoroutinesApi::class)
    override fun crack(crackRequest: CrackRequestDto) = CrackResponseDto(
        requestId = newRequestId().also {
            GlobalScope.launch { takeInWork(it, crackRequest, partCount) }
        }
    )

    private fun takeInWork(
        requestId: String,
        crackRequest: CrackRequestDto,
        partCount: Int,
    ) {
        taskRepo[requestId] = CrackParam(crackRequest)
        responseRepo.prepareForResponse(requestId, partCount)
        sendService.execute()
    }

    private fun newRequestId() = UUID.randomUUID().toString()

    override fun status(requestId: String) =
        if (responseRepo.isFinished(requestId))
            StatusResposeDto(
                Status.READY,
                responseRepo[requestId]
            )
        else
            StatusResposeDto(
                Status.IN_PROGRESS,
                null
            )
}