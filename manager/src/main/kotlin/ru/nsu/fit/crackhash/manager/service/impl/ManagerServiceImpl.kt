package ru.nsu.fit.crackhash.manager.service.impl

import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.dto.CrackRequestDto
import ru.nsu.fit.crackhash.manager.model.dto.CrackResponseDto
import ru.nsu.fit.crackhash.manager.model.dto.Status
import ru.nsu.fit.crackhash.manager.model.dto.StatusResposeDto
import ru.nsu.fit.crackhash.manager.model.entity.CrackParam
import ru.nsu.fit.crackhash.manager.repo.TaskRepo
import ru.nsu.fit.crackhash.manager.repo.ResponseRepo
import ru.nsu.fit.crackhash.manager.service.ManagerService
import ru.nsu.fit.crackhash.manager.worker.WorkerApi
import kotlin.random.Random

@Service
class ManagerServiceImpl(
    private val taskRepo: TaskRepo,
    private val responseRepo: ResponseRepo,
    private val workers: List<WorkerApi>
) : ManagerService {
    override fun crack(crackRequest: CrackRequestDto) = CrackResponseDto(
        requestId = randomString().let {
            taskRepo[it] = CrackParam(crackRequest)
            responseRepo.setDelayCountForResponse(it, workers.size)
            it
        }
    )

    private fun randomString() = String(Random.nextBytes(ByteArray(128)))

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