package ru.nsu.fit.crackhash.worker.config

import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.stream.binder.rabbit.config.RabbitConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(RabbitConfiguration::class)
@EnableRabbit
class RabbitConfig {
    @Bean
    fun queueManagerToWorker() = Queue("manager-to-worker", true)

    @Bean
    fun directExchangeManagerToWorker() = DirectExchange("manager-to-worker")
    // настроить автоак на листенере и он сам понимает, когда произошё   отвал
    // ещё сделать так, чтобы worker, брал ровно одну задачу
    // dlq не нужен, потому что вот эти ситуации, которые перечислены в статье не наш случай

    @Bean
    fun workerBinding(
        @Qualifier("queueManagerToWorker") queue: Queue,
        @Qualifier("directExchangeManagerToWorker") direct: DirectExchange,
    ) = BindingBuilder.bind(queue)
        .to(direct)
        .with("worker")

    @Bean
    fun messageConverter() = Jackson2JsonMessageConverter()
}