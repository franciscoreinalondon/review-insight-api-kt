package com.franciscoreina.reviewinsight.model.dto

import com.franciscoreina.reviewinsight.model.domain.Review
import com.franciscoreina.reviewinsight.model.domain.ReviewAnalysis

data class InsightResponse(
    val reviews: List<Review>,
    val total: Int,
    val positive: Int,
    val neutral: Int,
    val negative: Int,
    val analysis: ReviewAnalysis
)