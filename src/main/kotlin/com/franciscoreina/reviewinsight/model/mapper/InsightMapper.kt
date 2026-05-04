package com.franciscoreina.reviewinsight.model.mapper

import com.franciscoreina.reviewinsight.model.domain.ReviewInsight
import com.franciscoreina.reviewinsight.model.dto.InsightResponse

fun ReviewInsight.toResponse(): InsightResponse {
    return InsightResponse(
        reviews = this.reviews,
        total = this.stats.total,
        positive = this.stats.positive,
        neutral = this.stats.neutral,
        negative = this.stats.negative,
        analysis = this.analysis
    )
}