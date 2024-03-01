package ru.nsu.fit.crackhash.worker.config

import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.stream.binder.rabbit.config.RabbitConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(RabbitConfiguration::class)
@EnableRabbit
class RabbitConfig(@Value("\${worker.number}") private val workerNumber: String) {
    val QUEUE_DLX = "dlx-$workerNumber"

    @Bean
    fun deadLetterExchangeQueue() = DirectExchange(QUEUE_DLX)

    @Bean
    fun queueManagerToWorker() = Queue("manager-to-worker", true)

    @Bean
    fun queueWorkerToManager() = QueueBuilder.durable("worker-to-manager")
        .withArgument("x-dead-letter-exchange", QUEUE_DLX)
        .withArgument("x-dead-letter-routing-key", "deadLetter")
        .build()

    @Bean
    fun directExchangeManagerToWorker() = DirectExchange("manager-to-worker")

    @Bean
    fun directExchangeWorkerToManager() = DirectExchange("worker-to-manager")

    @Bean
    fun workerBinding(
        @Qualifier("queueManagerToWorker") queue: Queue,
        @Qualifier("directExchangeManagerToWorker") direct: DirectExchange,
    ) = BindingBuilder.bind(queue)
        .to(direct)
        .with(workerNumber)

    @Bean
    fun messageConverter() = Jackson2JsonMessageConverter()
}