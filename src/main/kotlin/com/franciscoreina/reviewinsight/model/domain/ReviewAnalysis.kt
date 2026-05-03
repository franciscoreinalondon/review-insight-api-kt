package com.franciscoreina.reviewinsight.model.domain

data class ReviewAnalysis(
    val summary: String,
    val topProblems: List<Problem>
)