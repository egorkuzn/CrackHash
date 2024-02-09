package ru.nsu.fit.crackhash.manager.worker

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.nsu.fit.crackhash.manager.model.dto.WorkerTaskDto


interface WorkerApi {
    @POST("worker/hash/crack/task")
    suspend fun giveTask(@Body crackRequest: WorkerTaskDto): Call<Unit>
}