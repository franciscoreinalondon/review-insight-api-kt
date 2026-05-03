package com.franciscoreina.reviewinsight.client

import com.franciscoreina.reviewinsight.model.domain.Review
import com.franciscoreina.reviewinsight.model.domain.ReviewAnalysis

interface ReviewAnalyzer {

    fun analyze(reviews: List<Review>): ReviewAnalysis

}