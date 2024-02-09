package ru.nsu.fit.crackhash.worker.service.impl

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.paukov.combinatorics3.Generator
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.worker.manager.ManagerApi
import ru.nsu.fit.crackhash.worker.model.enity.WorkerTask
import ru.nsu.fit.crackhash.worker.service.TaskExecutorService
import java.security.MessageDigest

@Service
class TaskExecutorServiceImpl(private val manager: ManagerApi) : TaskExecutorService {
    private val taskExecutorScope = MainScope()

    override fun takeNewTask(workerTask: WorkerTask) {
        taskExecutorScope.launch {
            manager.sendTaskResult(workerTask.requestId to executeTask(workerTask))
        }
    }

    private fun executeTask(workerTask: WorkerTask): Array<String> {
        var counter = 0
        return Generator.permutation(
            ('0'..'9') + ('a'..'z') + ('A'..'Z')
        ).withRepetitions(workerTask.maxLength)
            .stream()
            .skip(workerTask.partNumber.toLong())
            .filter { counter++ % workerTask.partCount == 0 }
            .parallel()
            .map {
                val generation = String(it.toCharArray())

                if (hash(generation) == workerTask.hash)
                    generation
                else
                    null
            }.filter { it != null }
            .toArray() as Array<String>
    }

    private val md5 = MessageDigest.getInstance("MD5")

    @OptIn(ExperimentalStdlibApi::class)
    private fun hash(it: String) = md5.run{
        update(it.toByteArray())
        digest().toHexString()
    }
}