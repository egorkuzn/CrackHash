package ru.nsu.fit.crackhash.manager.service.impl

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.dto.WorkerRequestDto
import ru.nsu.fit.crackhash.manager.repo.TaskRepo
import ru.nsu.fit.crackhash.manager.service.SendService
import ru.nsu.fit.crackhash.manager.worker.WorkerApi
import java.util.concurrent.ConcurrentHashMap

@Service
class SendServiceImpl(
    private val workers: List<WorkerApi>,
    private val taskRepo: TaskRepo,
) : SendService {
    private val freeWorkersMap = ConcurrentHashMap<WorkerApi, Boolean>()

    override fun execute() = runBlocking {
        val task = taskRepo.takeTask()

        var count = 0
        workers.associateBy { count++ }.forEach {
            launch {
                it.value.giveTask(
                    WorkerRequestDto(
                        task.hash,
                        task.maxLength,
                        task.requestId,
                        it.key,
                        workers.size
                    )
                )
            }
        }
    }
}