package com.franciscoreina.reviewinsight.model.domain

import java.time.LocalDateTime

data class Review(
    val author: String,
    val rating: Int,
    val title: String,
    val content: String,
    val voteCount: Int,
    val date: LocalDateTime,
    val sentiment: Sentiment
)