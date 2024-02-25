package ru.nsu.fit.crackhash.manager.controller

import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.nsu.fit.crackhash.manager.model.dto.WorkerResponseDto
import ru.nsu.fit.crackhash.manager.service.ManagerInternalService

@RestController
@RequestMapping("/internal/api/manager/hash/crack/request")
class ManagerInternalController(private val managerInternalService: ManagerInternalService) {
    @PatchMapping
    fun crackRequest(@RequestBody response: WorkerResponseDto) = managerInternalService.crackRequest(response)
}