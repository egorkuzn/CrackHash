package ru.nsu.fit.crackhash.worker.manager

import retrofit2.http.PATCH

interface ManagerApi {
    @PATCH("manager/hash/crack/request")
    fun sendTaskResult(result: Pair<String, Array<String>>)
}