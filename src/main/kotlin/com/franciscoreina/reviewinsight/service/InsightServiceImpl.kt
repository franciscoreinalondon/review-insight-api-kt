package com.franciscoreina.reviewinsight.service

import com.franciscoreina.reviewinsight.client.ReviewAnalyzer
import com.franciscoreina.reviewinsight.model.domain.Insight
import com.franciscoreina.reviewinsight.model.domain.Review
import org.springframework.stereotype.Service

@Service
class InsightServiceImpl(
    private val reviewAnalyzer: ReviewAnalyzer
) : InsightService {

    override fun generateInsight(reviews: List<Review>): Insight {
        return reviewAnalyzer.analyze(reviews)
    }

}