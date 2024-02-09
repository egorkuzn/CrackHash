package ru.nsu.fit.crackhash.manager.service

interface ManagerInternalService {
    fun crackRequest(request: Pair<String, Array<String>>)
}