package ru.nsu.fit.crackhash.manager.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Duration
import java.time.LocalDateTime

@Document("worker_task")
data class TaskMongoEntity(
    @Id
    val requestId: String,
    val hash: String,
    val maxLength: Int,
    val partCount: Int,
    var sendSet: Set<Int>,
    var resultSet: Set<String>,
    var taskStatus: TaskStatus = TaskStatus.WAIT,
    val time: LocalDateTime = LocalDateTime.now()
) {
    fun isTimeout(timeout: Int) = Duration.between(
        LocalDateTime.now(),
        time
    ).toMinutes() > timeout

    fun isToResend() = taskStatus == TaskStatus.WAIT
}