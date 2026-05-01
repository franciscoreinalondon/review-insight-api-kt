package com.franciscoreina.reviewinsight.model.domain

import java.time.OffsetDateTime

data class Review(
    val author: String,
    val rating: Int,
    val title: String,
    val content: String,
    val voteCount: Int,
    val date: OffsetDateTime,
    val sentiment: Sentiment
)