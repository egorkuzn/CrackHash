package ru.nsu.fit.crackhash.manager.config

import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.stream.binder.rabbit.config.RabbitConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * Creates Rabbit queues.
 *
 * Queue "manage-to-worker" send messages from Manager to Worker.
 *
 * Queue "worker-to-manager" get massages from Worker on Manager.
 *
 * Also, we use Json for communication between services.
 */
@Configuration
@Import(RabbitConfiguration::class)
@EnableRabbit
class RabbitConfig {
    val QUEUE_DLX = "queue-dlx"
    val QUEUE_DLQ = "queue-dlq"

    @Bean
    fun deadLetterExchangeQueue() = Queue(QUEUE_DLX, true)

    @Bean
    fun deadLetterRoatingKey() = Queue(QUEUE_DLQ, true)

    @Bean
    fun queueManagerToWorker() = QueueBuilder.durable("manager-to-worker")
        .withArgument("x-dead-letter-exchange", QUEUE_DLX)
        .withArgument("x-dead-letter-routing-key", QUEUE_DLQ)
        .build()

    @Bean
    fun queueWorkerToManager() = Queue("worker-to-manager", true)

    @Bean
    fun directExchangeManagerToWorker() = DirectExchange("manager-to-worker")

    @Bean
    fun directExchangeWorkerToManager() = DirectExchange("worker-to-manager")

    @Bean
    fun workerBinding(
        @Qualifier("queueWorkerToManager") queue: Queue,
        @Qualifier("directExchangeWorkerToManager") direct: DirectExchange,
    ) = BindingBuilder.bind(queue)
        .to(direct)
        .with("manager")

    @Bean
    fun messageConverter() = Jackson2JsonMessageConverter()
}