package ru.nsu.fit.crackhash.manager.worker

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import ru.nsu.fit.crackhash.manager.model.dto.WorkerRequestDto


interface WorkerApi {
    @PATCH("hash/crack")
    fun giveTask(@Body crackRequest: WorkerRequestDto)

    @GET("hash/crack")
    fun getTaskResults(): Map<String, Array<String>>
}