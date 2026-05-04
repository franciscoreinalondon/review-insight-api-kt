package com.franciscoreina.reviewinsight.client.apple.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Bean
    fun appleWebClient(builder: WebClient.Builder): WebClient {
        return builder
            .baseUrl("https://itunes.apple.com")
            .build()
    }

}