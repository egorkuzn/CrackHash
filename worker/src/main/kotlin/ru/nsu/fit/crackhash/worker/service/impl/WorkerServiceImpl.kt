package ru.nsu.fit.crackhash.worker.service.impl

import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.worker.model.dto.CrackRequestDto
import ru.nsu.fit.crackhash.worker.service.WorkerService

@Service
class WorkerServiceImpl: WorkerService {
    override fun crack(crackRequest: CrackRequestDto): Array<String> {
        TODO("Not yet implemented")
    }
}