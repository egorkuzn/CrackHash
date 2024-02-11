package ru.nsu.fit.crackhash.manager.service.impl

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.dto.WorkerResponseDto
import ru.nsu.fit.crackhash.manager.service.ManagerInternalService

@Service
class ManagerInternalServiceImpl(
    @Value("\${workers.count}")
    private val partCount: Int,
    private val logger: Logger,
    private val responseService: ResponseService,
) : ManagerInternalService {
    @OptIn(DelicateCoroutinesApi::class)
    override fun crackRequest(response: WorkerResponseDto) {
        if (isTimeout(response)) {
            logger.info("Got result [${response.partNumber}|$partCount] of ${response.requestId} TIMEOUT")
            responseService.workerTimeout(response.requestId)
            return
        }


        logger.info("Got result [${response.partNumber}|$partCount] of ${response.requestId} SUCCESS")
        GlobalScope.launch {
            responseService.putAll(response)
        }
    }

    private fun isTimeout(response: WorkerResponseDto) = response.value == null
}