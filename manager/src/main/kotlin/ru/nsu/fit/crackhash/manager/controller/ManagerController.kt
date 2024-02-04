package ru.nsu.fit.crackhash.manager.controller

import org.springframework.web.bind.annotation.*
import ru.nsu.fit.crackhash.manager.model.dto.CrackRequestDto
import ru.nsu.fit.crackhash.manager.service.ManagerService

@RestController
@RequestMapping("/api/hash")
class ManagerController(private val managerService: ManagerService) {
    @PostMapping("/crack")
    fun crack(@RequestBody crackRequest: CrackRequestDto) = managerService.crack(crackRequest)

    @GetMapping("/status")
    fun status(@RequestParam requestId: String) = managerService.status(requestId)
}