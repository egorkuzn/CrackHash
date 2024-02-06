package ru.nsu.fit.crackhash.manager.worker

import retrofit2.http.Body
import retrofit2.http.PATCH
import ru.nsu.fit.crackhash.manager.model.dto.WorkerRequestDto


interface WorkerApi {
    @PATCH("worker/hash/crack/task")
    fun giveTask(@Body crackRequest: WorkerRequestDto)
}