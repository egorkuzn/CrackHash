package ru.nsu.fit.crackhash.worker.config.scheduler

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import ru.nsu.fit.crackhash.worker.service.TaskService

@EnableAsync
@Configuration
@EnableScheduling
class WorkerScheduler(
    private val taskService: TaskService,
) {
    @Async
    @Scheduled(cron = "\${scheduler.interval}")
    fun taskRun() {
        taskService.execute()
    }
}