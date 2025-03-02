package com.nophasenokill.polymarket.config

import jakarta.ws.rs.client.Client
import org.glassfish.jersey.client.JerseyClientBuilder
import org.glassfish.jersey.server.ResourceConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class JerseyClientConfig {
    @Bean
    fun jerseyClient(): Client {
        return JerseyClientBuilder.newClient()
    }
}

@Configuration
class JerseyConfig : ResourceConfig() {
    init {
        packages("com.nophasenokill.polymarket")
    }
}
