package ru.nsu.fit.crackhash.manager.service.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.dto.CrackRequestDto
import ru.nsu.fit.crackhash.manager.model.dto.CrackResponseDto
import ru.nsu.fit.crackhash.manager.model.dto.Status
import ru.nsu.fit.crackhash.manager.model.dto.StatusResposeDto
import ru.nsu.fit.crackhash.manager.model.entity.TaskMongoEntity
import ru.nsu.fit.crackhash.manager.model.entity.TaskStatus
import ru.nsu.fit.crackhash.manager.repo.MongoTaskRepo
import ru.nsu.fit.crackhash.manager.service.ManagerService
import ru.nsu.fit.crackhash.manager.service.SendService
import java.util.*

@Service
class ManagerServiceImpl(
    @Value("\${workers.count}")
    private val partCount: Int,
    private val taskRepo: MongoTaskRepo,
    private val sendService: SendService
) : ManagerService {
    val managerCoroutineScope = CoroutineScope(Dispatchers.Default)

    override fun crack(crackRequest: CrackRequestDto) = CrackResponseDto(
        requestId = newRequestId().also {
            managerCoroutineScope.launch {
                takeInWork(it, crackRequest, partCount)
            }
        }
    )

    private fun takeInWork(
        requestId: String,
        crackRequest: CrackRequestDto,
        partCount: Int,
    ) {
        prepareTasks(
            requestId,
            crackRequest,
            partCount
        )
        sendService.execute(requestId)
    }

    private fun prepareTasks(
        requestId: String,
        crackRequest: CrackRequestDto,
        partCount: Int
    ) {
        taskRepo.save(
            TaskMongoEntity(
                requestId,
                crackRequest.hash,
                crackRequest.maxLength,
                partCount
            )
        )
    }

    private fun newRequestId() = UUID.randomUUID().toString()

    override fun status(requestId: String) = taskRepo.findFirstByRequestId(requestId).let {
        StatusResposeDto(
            it.taskStatus,
            if (it.taskStatus == TaskStatus.FINISHED)
                it.resultSet.toTypedArray()
            else
                null
        )
    }
}