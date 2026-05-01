package com.franciscoreina.reviewinsight.client.apple.mapper

import com.franciscoreina.reviewinsight.client.apple.dto.AppleReviewDTO
import com.franciscoreina.reviewinsight.model.domain.Review
import com.franciscoreina.reviewinsight.model.domain.Sentiment
import java.time.LocalDateTime

fun AppleReviewDTO.toDomain(): Review {
    return Review(
        author = this.author.name.label,
        rating = this.rating.label.toInt(),
        title = this.title.label,
        content = this.content.label,
        voteCount = this.voteCount.label.toInt(),
        date = LocalDateTime.now(),
        sentiment = Sentiment.NEGATIVE
    )
}