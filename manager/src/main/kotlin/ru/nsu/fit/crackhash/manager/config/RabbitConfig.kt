package ru.nsu.fit.crackhash.manager.config

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.cloud.stream.binder.rabbit.config.RabbitConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * Creates Rabbit queues.
 *
 * Queue "manage-to-worker" send massages from Manager to Worker.
 *
 * Queue "worker-to-manager" get massages from Worker on Manager.
 *
 * Also, we use Json for communication between services.
 */
@Configuration
@Import(RabbitConfiguration::class)
@EnableRabbit
class RabbitConfig {
    @Bean(name = ["manager-to-worker"])
    fun queueManagerToWorker() = Queue("manage-to-worker", true)

    @Bean(name = ["worker-to-manager"])
    fun queueWorkerToManager() = Queue("worker-to-manager", true)

    @Bean
    fun messageConverter() = Jackson2JsonMessageConverter()
}