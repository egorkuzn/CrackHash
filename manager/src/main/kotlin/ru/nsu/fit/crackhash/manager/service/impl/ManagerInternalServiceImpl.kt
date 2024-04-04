package ru.nsu.fit.crackhash.manager.service.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.OptimisticLockingFailureException
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
    private val taskRepo: MongoTaskRepo,
) : ManagerInternalService {
    override fun crackRequest(
        response: WorkerResponseDto,
        retryCount: Int,
    ) {
        try {
            taskUpdater(response)
        } catch (e: OptimisticLockingFailureException) {
            if (retryCount > 1) {
                logger.info("Request ${response.requestId} [${response.partNumber}|2] try to save $retryCount")
                crackRequest(response, retryCount - 1)
            } else {
                logger.info("Request ${response.requestId} [${response.partNumber}|2] failed")
                taskRepo.save(
                    taskRepo.findFirstByRequestId(response.requestId)
                        .apply {
                            taskStatus = TaskStatus.ERROR
                        }
                )
            }
        }

    }

    private fun taskUpdater(response: WorkerResponseDto) {

        logger.info("Find start")
        val task = taskRepo.findFirstByRequestId(response.requestId)
        logger.info("Find end")

        if (task.taskStatus == TaskStatus.IN_PROGRESS)
            task.apply {
                logger.info("Apply start")
                responseList[response.partNumber - 1] = true

                taskStatus = if (isTimeout(timeout)) TaskStatus.ERROR
                else {
                    if (response.value != null) resultSet = resultSet + response.value
                    if (responseList.contains(false)) taskStatus else TaskStatus.READY
                }

                logger.info("Request $requestId [${response.partNumber}|$partCount] $taskStatus")
            }.let {
                logger.info("BIM")
                taskRepo.save(it)
                logger.info("BAM")
            }
    }
}
