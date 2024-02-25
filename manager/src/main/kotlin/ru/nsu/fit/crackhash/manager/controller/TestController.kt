package ru.nsu.fit.crackhash.manager.controller

import org.springframework.amqp.core.AmqpTemplate
import org.springframework.amqp.core.Queue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.nsu.fit.crackhash.manager.model.dto.WorkerTaskDto

@RestController
@RequestMapping("/test")
class TestController(
    private val template: AmqpTemplate,
    private val queue: Queue
) {
    @GetMapping
    fun getTest() {
        template.convertAndSend(queue.name, WorkerTaskDto(
            "Привет дружище!",
            0,
            "",
            0,
            0
        ))
    }
}