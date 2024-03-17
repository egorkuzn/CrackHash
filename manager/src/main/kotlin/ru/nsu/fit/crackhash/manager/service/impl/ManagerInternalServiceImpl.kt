package ru.nsu.fit.crackhash.manager.service.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.dto.WorkerResponseDto
import ru.nsu.fit.crackhash.manager.model.entity.TaskStatus
import ru.nsu.fit.crackhash.manager.repo.MongoTaskRepo
import ru.nsu.fit.crackhash.manager.service.ManagerInternalService

@Service
class ManagerInternalServiceImpl(
    @Value("\${workers.timeout}")
    private val timeout: Int,
    private val logger: Logger,
    private val taskRepo: MongoTaskRepo
) : ManagerInternalService {
    val managerInternalCoroutineScope = CoroutineScope(Dispatchers.Default)

    override fun crackRequest(response: WorkerResponseDto) {
        managerInternalCoroutineScope.launch {
            when {
                response.value == null -> onTimeout(response)
                response.value.isNotEmpty() -> onNewValueResult(response)
            }
        }
    }

    private fun onTimeout(response: WorkerResponseDto) {
        val task = taskRepo.findFirstByRequestId(response.requestId)
        taskRepo.save(task.apply { taskStatus = TaskStatus.TIMEOUT })
    }

    private fun onNewValueResult(response: WorkerResponseDto) {
        val task = taskRepo.findFirstByRequestId(response.requestId)

        taskRepo.save(task.apply {
            if (task.isTimeout(timeout)) taskStatus = TaskStatus.TIMEOUT
            resultSet = resultSet + response.value!!
        })
    }
}