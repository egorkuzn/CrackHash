package ru.nsu.fit.crackhash.manager.service.impl

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.repo.ResponseRepo
import ru.nsu.fit.crackhash.manager.service.RecieveService
import ru.nsu.fit.crackhash.manager.worker.WorkerApi

@Service
class RecieveServiceImpl(
    private val workers: List<WorkerApi>,
    private val responseRepo: ResponseRepo
): RecieveService {
    override fun execute() = runBlocking {
        workers.forEach { worker->
            launch {
                responseRepo.putAll(worker.getTaskResults())
            }
        }
    }
}