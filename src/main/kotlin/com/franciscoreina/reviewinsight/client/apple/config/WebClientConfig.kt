package com.franciscoreina.reviewinsight.client.apple.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.codec.json.JacksonJsonDecoder
import org.springframework.web.reactive.function.client.WebClient
import tools.jackson.databind.json.JsonMapper

@Configuration
class WebClientConfig {

    @Bean
    fun appleWebClient(jsonMapper: JsonMapper): WebClient {
        return WebClient.builder()
            .baseUrl("https://itunes.apple.com")
            .codecs { configurer ->
                configurer.defaultCodecs().jacksonJsonDecoder(
                    JacksonJsonDecoder(jsonMapper, MediaType.parseMediaType("text/javascript"))
                )
            }
            .build()
    }

    @Bean
    fun openAiWebClient(@Value("\${openai.api-key}") apiKey: String): WebClient {
        return WebClient.builder()
            .baseUrl("https://api.openai.com")
            .defaultHeader("Authorization", "Bearer $apiKey")
            .defaultHeader("Content-Type", "application/json")
            .build()
    }

}