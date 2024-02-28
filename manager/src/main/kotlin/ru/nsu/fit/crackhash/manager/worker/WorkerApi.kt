package ru.nsu.fit.crackhash.manager.worker

import ru.nsu.fit.crackhash.manager.model.dto.WorkerTaskDto


interface WorkerApi {
    suspend fun takeTask(crackRequest: WorkerTaskDto): Unit
}