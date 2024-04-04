package ru.nsu.fit.crackhash.manager.service

import ru.nsu.fit.crackhash.manager.model.dto.WorkerResponseDto

interface ManagerInternalService {
    fun crackRequest(response: WorkerResponseDto, retryCount: Int = 5)
}