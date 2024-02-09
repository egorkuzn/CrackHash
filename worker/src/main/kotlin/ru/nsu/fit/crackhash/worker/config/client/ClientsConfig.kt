package ru.nsu.fit.crackhash.worker.config.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.nsu.fit.crackhash.worker.manager.ManagerApi

@Configuration
class ClientsConfig(
    @Value("\${manager.url}")
    private val url: String
) {
    @Bean
    fun getManager() =
        Retrofit.Builder()
            .baseUrl("$url/internal/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create() as ManagerApi
}