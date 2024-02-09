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
        requestId = newRequestId().let {
            GlobalScope.launch {
                taskRepo[it] = CrackParam(crackRequest)
                responseRepo.setDelayCountForResponse(it, partCount)
                sendService.execute()
            }

            it
        }
    )

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