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
        managerInternalCoroutineScope.launch { taskUpdater(response) }
    }

    private fun taskUpdater(response: WorkerResponseDto) = taskRepo.save(
        taskRepo.findFirstByRequestId(response.requestId).apply {
            receivedTaskCounter++

            taskStatus = if (response.value == null || isTimeout(timeout)) TaskStatus.ERROR
            else {
                resultSet = resultSet + response.value
                if (receivedTaskCounter == partCount) TaskStatus.READY else taskStatus
            }

            logger.info("Request $requestId [${response.partNumber}|$partCount] $taskStatus")
        }
    )
}
