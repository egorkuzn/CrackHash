package ru.nsu.fit.crackhash.manager.service

interface ManagerInternalService {
    fun crackRequest(request: Map<String, Array<String>>)
}