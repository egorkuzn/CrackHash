package ru.nsu.fit.crackhash.manager.controller

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import ru.nsu.fit.crackhash.manager.model.dto.WorkerResponseDto
import ru.nsu.fit.crackhash.manager.service.ManagerInternalService

@Component
class ManagerInternalController(private val managerInternalService: ManagerInternalService) {
    @RabbitListener(queues = ["worker-to-manager"])
    fun crackRequest(response: WorkerResponseDto) = managerInternalService.crackRequest(response)
}