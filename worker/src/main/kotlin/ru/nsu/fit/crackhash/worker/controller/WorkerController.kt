package ru.nsu.fit.crackhash.worker.controller

import org.springframework.web.bind.annotation.*
import ru.nsu.fit.crackhash.worker.model.dto.CrackRequestDto
import ru.nsu.fit.crackhash.worker.service.WorkerService

@RestController
@RequestMapping("/internal/api/worker/hash/crack/task")
class WorkerController(private val workerService: WorkerService) {
    @PostMapping
    fun takeTask(@RequestBody crackRequest: CrackRequestDto) = workerService.takeTask(crackRequest)
}