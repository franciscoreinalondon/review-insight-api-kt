package com.franciscoreina.reviewinsight.model.domain

enum class Sentiment {
    POSITIVE,
    NEGATIVE,
    NEUTRAL,
    UNKNOWN;

    companion object {
        fun fromRating(rating: Int?): Sentiment {
            return when {
                rating == null -> UNKNOWN
                rating >= 4 -> POSITIVE
                rating <= 2 -> NEGATIVE
                else -> NEUTRAL
            }
        }
    }
}