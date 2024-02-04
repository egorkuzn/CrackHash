package ru.nsu.fit.crackhash.worker.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.nsu.fit.crackhash.worker.model.dto.CrackRequestDto
import ru.nsu.fit.crackhash.worker.service.WorkerService

@RestController
@RequestMapping("/api/hash")
class WorkerController(private val workerService: WorkerService) {
    @GetMapping("/crack")
    fun crack(@RequestBody crackRequest: CrackRequestDto) = workerService.crack(crackRequest)
}