package ru.nsu.fit.crackhash.manager.config

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.cloud.stream.binder.rabbit.config.RabbitConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import


@Configuration
@Import(RabbitConfiguration::class)
@EnableRabbit
class RabbitConfig {
    @Bean
    fun queue() = Queue("test-queue", true)
    @Bean
    fun messageConverter() = Jackson2JsonMessageConverter()
}