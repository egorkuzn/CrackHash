package ru.nsu.fit.crackhash.manager.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
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
    var sendSet: Set<Int> = (1 .. partCount).toSet(),
    val responseList: ArrayList<Boolean> = ArrayList(List(partCount) {false}),
    var resultSet: Set<String> = mutableSetOf(),
    var taskStatus: TaskStatus = TaskStatus.IN_PROGRESS,
    val time: LocalDateTime = LocalDateTime.now(),
    @Version
    var version: Int = 0
) {
    fun isTimeout(timeout: Int) = Duration.between(
        LocalDateTime.now(),
        time
    ).toMinutes() > timeout

    fun isToResend() = taskStatus == TaskStatus.IN_PROGRESS
}