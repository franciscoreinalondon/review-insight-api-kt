package com.franciscoreina.reviewinsight.service

import com.franciscoreina.reviewinsight.client.ReviewAnalyzer
import com.franciscoreina.reviewinsight.client.ReviewProvider
import com.franciscoreina.reviewinsight.exceptions.EmptyReviewsException
import com.franciscoreina.reviewinsight.model.domain.Review
import com.franciscoreina.reviewinsight.model.domain.ReviewInsight
import com.franciscoreina.reviewinsight.model.domain.ReviewStats
import com.franciscoreina.reviewinsight.model.domain.Sentiment
import org.springframework.stereotype.Service

@Service
class InsightServiceImpl(
    private val reviewProvider: ReviewProvider,
    private val reviewAnalyzer: ReviewAnalyzer
) : InsightService {

    override fun generateInsight(appId: Int, country: String, pages: Int): ReviewInsight {

        val reviews = reviewProvider.fetchReviews(appId, country, pages)

        if (reviews.isEmpty()) {
            throw EmptyReviewsException("Reviews cannot be empty for app $appId")
        }

        val stats = reviews.calculateStats()

        val analysis = reviewAnalyzer.analyze(reviews)

        return ReviewInsight(
            reviews,
            stats,
            analysis
        )
    }

    private fun List<Review>.calculateStats() = ReviewStats(
        total = this.size,
        positive = this.count() { it.sentiment == Sentiment.POSITIVE },
        neutral = this.count() { it.sentiment == Sentiment.NEUTRAL },
        negative = this.count() { it.sentiment == Sentiment.NEGATIVE }
    )
}