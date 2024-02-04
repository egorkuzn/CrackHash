package ru.nsu.fit.crackhash.manager.config.scheduler

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@EnableAsync
@Configuration
@EnableScheduling
class ManagerScheduler {

    @Async
    @Scheduled(cron = "\${workers.scheduler.interval}")
    fun sendToWorkers() {

    }

    @Async
    @Scheduled(cron = "\${workers.scheduler.interval}")
    fun recieveFromWorkers() {

    }
}