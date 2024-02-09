package ru.nsu.fit.crackhash.manager.config.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.nsu.fit.crackhash.manager.model.entity.WorkerEntity
import ru.nsu.fit.crackhash.manager.worker.WorkerApi

@Configuration
class ClientsConfig(
    @Value("\${workers.base_url}")
    private val baseUrl: String,
    @Value("\${workers.count}")
    private val workersCount: Int,
) {
    @Bean
    fun getWorkers() = (1..workersCount).map { workerNumber ->
        WorkerEntity(
            Retrofit.Builder()
                .baseUrl("$baseUrl-$workerNumber/internal/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create() as WorkerApi,
            workerNumber
        )
    }
}