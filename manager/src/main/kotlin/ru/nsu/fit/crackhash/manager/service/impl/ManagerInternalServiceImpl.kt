package ru.nsu.fit.crackhash.manager.service.impl

import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.manager.repo.ResponseRepo
import ru.nsu.fit.crackhash.manager.service.ManagerInternalService

@Service
class ManagerInternalServiceImpl(private val responseRepo: ResponseRepo): ManagerInternalService {
    override fun crackRequest(request: Map<String, Array<String>>) {
        responseRepo.putAll(request)
    }
}