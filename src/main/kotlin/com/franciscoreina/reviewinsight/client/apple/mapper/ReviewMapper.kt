package com.franciscoreina.reviewinsight.client.apple.mapper

import com.franciscoreina.reviewinsight.client.apple.dto.AppleReviewDTO
import com.franciscoreina.reviewinsight.model.domain.Review
import com.franciscoreina.reviewinsight.model.domain.Sentiment
import java.time.OffsetDateTime

fun AppleReviewDTO.toDomain(): Review {
    return Review(
        author = this.author.name.label.trim(),
        rating = this.rating.label.toIntOrNull() ?: 0,
        title = this.title.label.trim(),
        content = this.content.label.trim(),
        voteCount = this.voteCount.label.toIntOrNull() ?: 0,
        date = OffsetDateTime.parse(this.updated.label),
        sentiment = Sentiment.fromRating(this.rating.label.toIntOrNull() ?: -1)
    )
}