package ru.nsu.fit.crackhash.worker.manager

import retrofit2.http.Body
import retrofit2.http.PATCH
import ru.nsu.fit.crackhash.worker.model.dto.WorkerResponseDto

interface ManagerApi {
    @PATCH("manager/hash/crack/request")
    suspend fun sendTaskResult(@Body result: WorkerResponseDto): Unit
}