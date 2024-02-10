package ru.nsu.fit.crackhash.manager.service.impl

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.dto.WorkerResponseDto
import ru.nsu.fit.crackhash.manager.repo.ResponseRepo
import ru.nsu.fit.crackhash.manager.service.ManagerInternalService

@Service
class ManagerInternalServiceImpl(
    @Value("\${workers.count}")
    private val partCount: Int,
    private val logger: Logger,
    private val responseRepo: ResponseRepo
): ManagerInternalService {
    override fun crackRequest(response: WorkerResponseDto) {
        logger.info("Got result [${response.partNumber}|$partCount] of ${response.responseId}")

        GlobalScope.launch {
            responseRepo.putAll(response)
        }
    }
}