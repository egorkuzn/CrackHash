package ru.nsu.fit.crackhash.worker.service

import ru.nsu.fit.crackhash.worker.model.dto.CrackRequestDto

interface WorkerService {
    fun takeTasks(crackRequest: CrackRequestDto)
    fun getTasksResults(): Map<String, Array<String>>
}