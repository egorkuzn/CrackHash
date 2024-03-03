package ru.nsu.fit.crackhash.manager.config

import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.connection.Connection
import org.springframework.amqp.rabbit.connection.ConnectionListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.stream.binder.rabbit.config.RabbitConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import ru.nsu.fit.crackhash.manager.model.dto.WorkerTaskDto
import ru.nsu.fit.crackhash.manager.model.entity.WorkerEntity
import ru.nsu.fit.crackhash.manager.worker.WorkerApi
import java.lang.Exception

// https://habr.com/ru/articles/703352/

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
class RabbitConfig(@Value("\${workers.count}") private val workersCount: Int) {
    @Bean
    fun queueWorkerToManager() = Queue("worker-to-manager", true)

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
    fun workers(template: RabbitTemplate): List<WorkerEntity> {
        template.connectionFactory.addConnectionListener(object: ConnectionListener{
            override fun onCreate(connection: Connection) {
             // TODO: добавить монго
            }
        })

        return (1..workersCount).map {
            WorkerEntity(
                object : WorkerApi {
                    override fun takeTask(task: WorkerTaskDto) {
                        template.convertAndSend(
                            "manager-to-worker",
                            "worker",
                            task
                        )
                    }
                },
                it
            )
        }
    }

    @Bean
    fun messageConverter() = Jackson2JsonMessageConverter()
}