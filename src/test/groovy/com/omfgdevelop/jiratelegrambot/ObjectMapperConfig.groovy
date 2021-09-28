package com.omfgdevelop.jiratelegrambot

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper

@Configuration
class ObjectMapperConfig {

    @Bean
    @Scope("prototype")
    ObjectMapper getObjectMapper() {
        return new ObjectMapper()
    }
}
