package ru.nsu.fit.crackhash.manager.service.impl

import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.model.entity.WorkerEntity
import ru.nsu.fit.crackhash.manager.service.WorkerIterableService

@Service
class WorkerIterableServiceImpl: WorkerIterableService {
    val workersList = emptyList<WorkerEntity>()

    override fun iterator(): Iterator<WorkerEntity> {
        return workersList.iterator()
    }
}