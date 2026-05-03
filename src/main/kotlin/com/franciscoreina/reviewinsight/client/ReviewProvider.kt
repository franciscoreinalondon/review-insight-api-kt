package com.franciscoreina.reviewinsight.client

import com.franciscoreina.reviewinsight.model.domain.Review

interface ReviewProvider {

    fun fetchReviews(appId: Int, country: String, pages: Int): List<Review>

}