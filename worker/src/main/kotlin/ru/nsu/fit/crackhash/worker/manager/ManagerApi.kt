package ru.nsu.fit.crackhash.worker.manager

import retrofit2.http.Body
import retrofit2.http.PATCH

interface ManagerApi {
    @PATCH("manager/hash/crack/request")
    suspend fun sendTaskResult(@Body result: Pair<String, Array<String>>): Unit
}