package ru.nsu.fit.crackhash.manager.model.entity

import ru.nsu.fit.crackhash.manager.worker.WorkerApi

data class WorkerEntity (
    val client: WorkerApi,
    val partNumber: Int
)