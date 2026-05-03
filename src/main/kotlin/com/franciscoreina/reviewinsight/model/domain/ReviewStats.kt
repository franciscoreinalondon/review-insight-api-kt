package com.franciscoreina.reviewinsight.model.domain

data class ReviewStats(
    val total: Int,
    val positive: Int,
    val neutral: Int,
    val negative: Int
)