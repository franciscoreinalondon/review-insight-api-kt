package com.franciscoreina.reviewinsight.model.dto

import com.franciscoreina.reviewinsight.model.domain.Insight
import com.franciscoreina.reviewinsight.model.domain.Review

data class InsightResponse(
    val totalReviews: Int,
    val positive: Int,
    val negative: Int,
    val neutral: Int,
    val reviews: List<Review>,
    val insight: Insight
)