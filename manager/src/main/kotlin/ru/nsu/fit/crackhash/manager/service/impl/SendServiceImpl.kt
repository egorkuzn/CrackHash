package ru.nsu.fit.crackhash.manager.service.impl

import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.repo.RequestRepo
import ru.nsu.fit.crackhash.manager.service.SendService
import ru.nsu.fit.crackhash.manager.worker.WorkerApi

@Service
class SendServiceImpl(
    private val workers: List<WorkerApi>,
    private val requestRepo: RequestRepo
): SendService {
    override fun execute() {
        // TODO: finish sender
    }
}