package com.franciscoreina.reviewinsight.model.domain

data class Problem(
    val name: String,
    val count: Int,
    val examples: List<String>
)