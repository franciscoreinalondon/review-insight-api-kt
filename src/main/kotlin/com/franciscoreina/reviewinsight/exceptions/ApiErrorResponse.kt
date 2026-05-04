package com.franciscoreina.reviewinsight.exceptions

import java.time.Instant

data class ApiErrorResponse(
    val status: Int,
    val code: String,
    val message: String,
    val timeStamp: Instant = Instant.now()
)