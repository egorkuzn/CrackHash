package ru.nsu.fit.crackhash.worker.service.impl

import org.paukov.combinatorics3.Generator
import org.springframework.stereotype.Service
import ru.nsu.fit.crackhash.worker.repo.TaskRepo
import ru.nsu.fit.crackhash.worker.service.TaskService

@Service
class TaskServiceImpl(private val taskRepo: TaskRepo): TaskService {
    private var maxLength = 0

    override fun execute() {
        val res = taskRepo.takeFirstTask() ?: return

        res.second.apply {
            val hash = hash
            val startWord: String = getStartWord(partNumber, partCount, maxLength)
            val finishWord: String = getFinishWord(partNumber, partCount, maxLength)


        }
    }

    fun execution(a: String, work: (String) -> Unit, maxLength: Int) {
        (1 .. maxLength).forEach { length ->
            Generator.combination(('0' .. '9') + ('a' .. 'z') + ('A' .. 'Z'))
                .multi(length)
                .stream()
                .forEach { work(String(it.toCharArray())) }
        }
    }
}