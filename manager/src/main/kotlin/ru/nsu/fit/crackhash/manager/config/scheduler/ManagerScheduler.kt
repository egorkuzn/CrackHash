package ru.nsu.fit.crackhash.manager.config.scheduler

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@EnableScheduling
class ManagerScheduler(
    @Value("\${workers.urls}")
    val workersUrls: Int
) {

    /**
     * Функция вызывается по расписанию для обновления данных о проектах Gitlab
     */
    @Scheduled(cron = "\${workers.scheduler.interval}")
    fun sendToWorkers() {

    }

    @Scheduled(cron = "\${workers.scheduler.interval}")
    fun recieveFromWorkers() {

    }
}