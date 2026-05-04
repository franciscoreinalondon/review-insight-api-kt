package com.franciscoreina.reviewinsight.client.apple

import com.franciscoreina.reviewinsight.client.ReviewProvider
import com.franciscoreina.reviewinsight.client.apple.dto.AppleReviewDTO
import com.franciscoreina.reviewinsight.client.apple.mapper.toDomain
import com.franciscoreina.reviewinsight.model.domain.Review
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(name = ["app.mock-mode"], havingValue = "false") // If mock disabled -> enables real API call
class AppleReviewProvider(
    private val rssClient: AppleRssClient,
) : ReviewProvider {

    override fun fetchReviews(
        appId: Int,
        country: String,
        pages: Int
    ): List<Review> {
        val reviews = mutableListOf<Review>()

        for (page in 1..pages) {
            val appleRssResponseDTO = rssClient.fetchReviews(appId, country, page)
            val pageReviews = appleRssResponseDTO.feed.entries.map(AppleReviewDTO::toDomain)
            reviews.addAll(pageReviews)
        }

        return reviews
    }
}