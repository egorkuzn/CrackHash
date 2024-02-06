package ru.nsu.fit.crackhash.manager.service.impl

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.dto.WorkerRequestDto
import ru.nsu.fit.crackhash.manager.repo.TaskRepo
import ru.nsu.fit.crackhash.manager.service.SendService
import ru.nsu.fit.crackhash.manager.worker.WorkerApi

@Service
class SendServiceImpl(
    private val workers: List<WorkerApi>,
    private val taskRepo: TaskRepo
): SendService {
    override fun execute() = runBlocking {
        val task = taskRepo.takeDerivedTask()

        task.execute { worker, partNumber, partCount, task, requestId ->
            launch {
                worker.giveTask(WorkerRequestDto(
                    task.hash,
                    task.maxLength,
                    requestId,
                    partNumber,
                    partCount
                ))
            }
        }
    }
}