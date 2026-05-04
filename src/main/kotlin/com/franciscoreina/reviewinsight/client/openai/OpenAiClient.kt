package com.franciscoreina.reviewinsight.client.openai

import com.franciscoreina.reviewinsight.client.openai.dto.OpenAiRequestDTO
import com.franciscoreina.reviewinsight.exceptions.ReviewAnalyzerException
import com.franciscoreina.reviewinsight.exceptions.ReviewProviderException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class OpenAiClient(@Qualifier("openAiWebClient") val openAiWebClient: WebClient) {

    private val logger = KotlinLogging.logger {}

    fun postAnalysis(request: OpenAiRequestDTO): String {
        try {
            return openAiWebClient.post()
                .uri("/v1/responses")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String::class.java)
                .block() ?: throw ReviewAnalyzerException("OpenAI returned empty body")

        } catch (ex: Exception) {
            logger.error(ex) { "Failed to analyze reviews" }
            throw ReviewAnalyzerException("Error connecting to OpenAi API", ex)
        }
    }
}