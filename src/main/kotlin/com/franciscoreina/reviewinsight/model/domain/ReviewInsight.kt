package com.franciscoreina.reviewinsight.model.domain

data class ReviewInsight(
    val reviews: List<Review>,
    val stats: ReviewStats,
    val analysis: ReviewAnalysis
)