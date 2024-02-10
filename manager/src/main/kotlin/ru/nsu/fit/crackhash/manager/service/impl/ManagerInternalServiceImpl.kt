package ru.nsu.fit.crackhash.manager.service.impl

import org.slf4j.Logger
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.dto.WorkerResponseDto
import ru.nsu.fit.crackhash.manager.repo.ResponseRepo
import ru.nsu.fit.crackhash.manager.service.ManagerInternalService

@Service
class ManagerInternalServiceImpl(
    private val logger: Logger,
    private val responseRepo: ResponseRepo
): ManagerInternalService {
    override fun crackRequest(response: WorkerResponseDto) {
        logger.info("Got result of ${response.responseId}")
        responseRepo.putAll(response)
    }
}