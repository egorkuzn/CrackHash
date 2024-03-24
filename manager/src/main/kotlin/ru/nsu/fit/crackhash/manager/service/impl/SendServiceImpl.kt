package ru.nsu.fit.crackhash.manager.service.impl

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.entity.TaskMongoEntity
import ru.nsu.fit.crackhash.manager.model.entity.TaskStatus
import ru.nsu.fit.crackhash.manager.repo.MongoTaskRepo
import ru.nsu.fit.crackhash.manager.service.SendService
import ru.nsu.fit.crackhash.manager.worker.WorkerApi

@Service
class SendServiceImpl(
    private val logger: Logger,
    private val workersPool: WorkerApi,
    private val taskRepo: MongoTaskRepo,
    private val template: RabbitTemplate,
    @Value("\${workers.timeout}") private val timeout: Int,
) : SendService {
    var sendServiceCoroutineScope = CoroutineScope(Dispatchers.Default)

    @PostConstruct
    override fun init() {
        rabbitListener()
    }

    override fun execute(requestId: String) {
        sendServiceCoroutineScope.launch {
            val task = taskRepo.findFirstByRequestId(requestId)

            (1..task.partCount).forEach { partNumber ->
                launch {
                    logger.info("Sending [$partNumber|${task.partCount}] $requestId")
                    workersPool.takeTask(task, partNumber)
                    logger.info("Sent [$partNumber|${task.partCount}] $requestId")
                }
            }
        }
    }

    override fun sendAfterRabbitReconnect(findAllByTaskStatus: List<TaskMongoEntity>) {
        findAllByTaskStatus.forEach { task ->
            when {
                task.isTimeout(timeout) -> taskRepo.save(task.apply { taskStatus = TaskStatus.ERROR })
                task.isToResend() -> task.sendSet.forEach { partNumber -> workersPool.takeTask(task, partNumber) }
            }
        }
    }

    private fun rabbitListener() {
        template.connectionFactory.addConnectionListener {
            logger.info("Rabbit reconnect")
            sendAfterRabbitReconnect(taskRepo.findAllByTaskStatus(TaskStatus.IN_PROGRESS))
        }
    }
}