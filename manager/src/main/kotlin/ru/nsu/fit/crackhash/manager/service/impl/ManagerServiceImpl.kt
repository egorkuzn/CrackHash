package ru.nsu.fit.crackhash.manager.service.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.dto.CrackRequestDto
import ru.nsu.fit.crackhash.manager.model.dto.CrackResponseDto
import ru.nsu.fit.crackhash.manager.model.dto.StatusResponseDto
import ru.nsu.fit.crackhash.manager.model.entity.TaskMongoEntity
import ru.nsu.fit.crackhash.manager.model.entity.TaskStatus
import ru.nsu.fit.crackhash.manager.repo.MongoTaskRepo
import ru.nsu.fit.crackhash.manager.service.ManagerService
import ru.nsu.fit.crackhash.manager.service.SendService
import java.util.*

@Service
class ManagerServiceImpl(
    @Value("\${workers.timeout}")
    private val timeout: Int,
    @Value("\${workers.count}")
    private val partCount: Int,
    private val taskRepo: MongoTaskRepo,
    private val sendService: SendService
) : ManagerService {
    val managerCoroutineScope = CoroutineScope(Dispatchers.Default)

    override fun crack(crackRequest: CrackRequestDto): CrackResponseDto = newRequestId().let { requestId ->
        takeInWork(requestId, crackRequest, partCount)
        CrackResponseDto(requestId)
    }

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

        managerCoroutineScope.launch { sendService.execute(requestId) }
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
        if (it.taskStatus == TaskStatus.IN_PROGRESS &&  it.isTimeout(timeout)) {
            taskRepo.save(it.apply { taskStatus = TaskStatus.ERROR })
            StatusResponseDto(TaskStatus.ERROR, null)
        } else {
            val data = if (it.taskStatus == TaskStatus.READY) it.resultSet.toTypedArray() else null
            StatusResponseDto(it.taskStatus, data)
        }
    }
}