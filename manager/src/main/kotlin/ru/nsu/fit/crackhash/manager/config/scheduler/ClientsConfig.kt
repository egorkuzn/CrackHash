package ru.nsu.fit.crackhash.manager.config.scheduler

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.nsu.fit.crackhash.manager.worker.WorkerApi

@Configuration
class ClientsConfig(
    @Value("\${workers.urls}")
    private val workers: Array<String>
) {
    @Bean
    fun getWorkers() = workers.map {
        Retrofit.Builder()
            .baseUrl("$it/api/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create()
                )
            ).build().create() as WorkerApi
    }
}