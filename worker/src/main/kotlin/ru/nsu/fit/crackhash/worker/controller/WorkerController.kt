package ru.nsu.fit.crackhash.worker.controller

import org.springframework.web.bind.annotation.*
import ru.nsu.fit.crackhash.worker.model.dto.WorkerTaskDto
import ru.nsu.fit.crackhash.worker.service.WorkerService

@RestController
@RequestMapping("/internal/api/worker/hash/crack/task")
class WorkerController(private val workerService: WorkerService) {
    @PostMapping
    fun takeTask(@RequestBody crackRequest: WorkerTaskDto) = workerService.takeTask(crackRequest)
}