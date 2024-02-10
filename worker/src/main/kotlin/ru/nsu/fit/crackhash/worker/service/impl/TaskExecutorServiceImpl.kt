package ru.nsu.fit.crackhash.worker.service.impl

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.paukov.combinatorics3.Generator
import org.slf4j.Logger
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.worker.manager.ManagerApi
import ru.nsu.fit.crackhash.worker.model.enity.WorkerTask
import ru.nsu.fit.crackhash.worker.service.TaskExecutorService
import java.security.MessageDigest
import kotlin.jvm.optionals.getOrElse

@Service
class TaskExecutorServiceImpl(
    private val logger: Logger,
    private val manager: ManagerApi
) : TaskExecutorService {
    @OptIn(DelicateCoroutinesApi::class)
    override fun takeNewTask(workerTask: WorkerTask) {
        GlobalScope.launch {
            manager.sendTaskResult(workerTask.requestId to executeTask(workerTask))
            workerTask.apply { logger.info("Task [$partNumber|$partCount]#$requestId finished") }
        }
    }

    private fun executeTask(workerTask: WorkerTask): Array<String> {
        var counter = 0

        workerTask.apply { logger.info("Task [$partNumber|$partCount]#$requestId started") }
        val res = Generator.permutation(
            ('0'..'9') + ('a'..'z') + ('A'..'Z')
        ).withRepetitions(workerTask.maxLength)
            .stream()
            .skip(workerTask.partNumber.toLong())
            .filter { counter++ % workerTask.partCount == 0 }
            .filter { hash(String(it.toCharArray())) == workerTask.hash }
            .findAny().getOrElse { return emptyArray() }.toCharArray().toString()

        return arrayOf(res)
    }

    private val md5 = MessageDigest.getInstance("MD5")

    @OptIn(ExperimentalStdlibApi::class)
    private fun hash(it: String) = md5.run {
        update(it.toByteArray())
        digest().toHexString()
    }
}