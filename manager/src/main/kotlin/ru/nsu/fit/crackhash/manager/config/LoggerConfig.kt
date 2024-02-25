package ru.nsu.fit.crackhash.manager.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InjectionPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@Configuration
class LoggerConfig {

    @Bean
    @Scope("prototype")
    fun produceLogger(injectionPoint: InjectionPoint): Logger =
        LoggerFactory.getLogger(injectionPoint.member.declaringClass)
}