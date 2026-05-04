package com.franciscoreina.reviewinsight.client.apple

import com.franciscoreina.reviewinsight.client.apple.dto.AppleRssResponseDTO
import com.franciscoreina.reviewinsight.exceptions.ReviewProviderException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class AppleRssClient(val webClient: WebClient) {

    private val logger = KotlinLogging.logger {}

    fun fetchReviews(appId: Int, country: String, page: Int): AppleRssResponseDTO {
        try {
            return webClient.get()
                .uri("/{country}/rss/customerreviews/page={page}/id={appId}/sortBy=mostRecent/json",
                    country, page, appId)
                .retrieve()
                .bodyToMono(AppleRssResponseDTO::class.java)
                .block() ?: throw ReviewProviderException("Apple RSS returned an empty body")
        } catch (ex: Exception) {
            logger.error(ex) { "Failed to fetch reviews for appId: $appId on page $page" }
            throw ReviewProviderException("Error connecting to Apple RSS API", ex)
        }
    }

}