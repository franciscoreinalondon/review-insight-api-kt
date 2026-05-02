package com.franciscoreina.reviewinsight.client

import com.franciscoreina.reviewinsight.model.domain.Insight
import com.franciscoreina.reviewinsight.model.domain.Review

interface ReviewAnalyzer {

    fun analyze(reviews: List<Review>): Insight

}