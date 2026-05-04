package com.franciscoreina.reviewinsight.client.apple

import com.franciscoreina.reviewinsight.client.apple.dto.AppleRssResponseDTO
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class AppleRssClient(val webClient: WebClient) {

    fun fetchReviews(appId: Int, country: String, pages: Int): AppleRssResponseDTO {
        return TODO()
    }

}