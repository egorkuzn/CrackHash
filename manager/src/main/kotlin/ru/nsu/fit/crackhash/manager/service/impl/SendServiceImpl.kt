package ru.nsu.fit.crackhash.manager.service.impl

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.dto.WorkerTaskDto
import ru.nsu.fit.crackhash.manager.model.entity.WorkerEntity
import ru.nsu.fit.crackhash.manager.repo.TaskRepo
import ru.nsu.fit.crackhash.manager.service.SendService

@Service
class SendServiceImpl(
    @Value("\${workers.count}")
    private val partCount: Int,
    private val workers: List<WorkerEntity>,
    private val taskRepo: TaskRepo,
) : SendService {
    override fun execute() = runBlocking {
        val task = taskRepo.takeTask()

        workers.forEach {
            launch {
                it.client.giveTask(
                    WorkerTaskDto(
                        task.hash,
                        task.maxLength,
                        task.requestId,
                        it.partNumber,
                        partCount
                    )
                ).execute()
            }
        }
    }
}