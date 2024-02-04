package ru.nsu.fit.crackhash.worker.config.scheduler

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@EnableAsync
@Configuration
@EnableScheduling
class WorkerScheduler {
    @Async
    @Scheduled(cron = "\${scheduler.interval}")
    fun sendToWorkers() {

    }

    @Async
    @Scheduled(cron = "\${scheduler.interval}")
    fun recieveFromWorkers() {

    }
}