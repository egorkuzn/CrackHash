package ru.nsu.fit.crackhash.manager.worker

import ru.nsu.fit.crackhash.manager.model.entity.TaskMongoEntity

interface WorkerApi {
    fun init()
    fun takeTask(task: TaskMongoEntity, partNumber: Int)
}