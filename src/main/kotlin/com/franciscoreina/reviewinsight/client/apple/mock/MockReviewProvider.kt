package com.franciscoreina.reviewinsight.client.apple.mock

import com.franciscoreina.reviewinsight.client.ReviewProvider
import com.franciscoreina.reviewinsight.client.apple.dto.AppleRssResponseDTO
import com.franciscoreina.reviewinsight.client.apple.mapper.toDomain
import com.franciscoreina.reviewinsight.model.domain.Review
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import tools.jackson.databind.json.JsonMapper

@Component
@ConditionalOnProperty(
    name = ["app.mock-mode"],
    havingValue = "true", // Activates mock implementation
)
class MockReviewProvider(private val jsonMapper: JsonMapper) : ReviewProvider {

    override fun fetchReviews(appId: Int, country: String, pages: Int): List<Review> {
        val jsonStream = ClassPathResource("mocks/apple/zopa-reviews.json").inputStream
        val response = jsonMapper.readValue(jsonStream, AppleRssResponseDTO::class.java)
        return response.feed.entries.map { it.toDomain() }
    }

}