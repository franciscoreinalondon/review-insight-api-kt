package com.franciscoreina.reviewinsight.service

import com.franciscoreina.reviewinsight.model.domain.Insight
import com.franciscoreina.reviewinsight.model.domain.Review

interface InsightService {

    fun generateInsight(reviews: List<Review>): Insight

}