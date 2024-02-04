package ru.nsu.fit.crackhash.manager.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.nsu.fit.crackhash.manager.model.dto.CrackRequestDto
import ru.nsu.fit.crackhash.manager.model.dto.CrackResponseDto
import ru.nsu.fit.crackhash.manager.model.dto.StatusResposeDto
import ru.nsu.fit.crackhash.manager.service.ManagerService

@RestController
@RequestMapping("api/hash/")
class ManagerController(private val managerService: ManagerService) {
    @PostMapping("/crack")
    fun crack(@RequestBody crackRequest: CrackRequestDto): CrackResponseDto {
        return managerService.crack(crackRequest)
    }

    @GetMapping("/status")
    fun status(@RequestParam requestId: String): StatusResposeDto {
        return managerService.status(requestId)
    }
}