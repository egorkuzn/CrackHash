package ru.nsu.fit.crackhash.worker.service

import ru.nsu.fit.crackhash.worker.model.enity.WorkerTask

interface TaskExecutorService {
    fun takeNewTask(workerTask: WorkerTask)
}