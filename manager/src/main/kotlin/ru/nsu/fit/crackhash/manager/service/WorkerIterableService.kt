package ru.nsu.fit.crackhash.manager.service

import ru.nsu.fit.crackhash.manager.model.entity.WorkerEntity

interface WorkerIterableService: Iterable<WorkerEntity> {
}