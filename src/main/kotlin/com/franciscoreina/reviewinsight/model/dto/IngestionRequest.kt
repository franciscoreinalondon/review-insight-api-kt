package com.franciscoreina.reviewinsight.model.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class IngestionRequest(
    val appId: Int,

    @field:NotBlank(message = "country is required")
    val country: String,

    @field:Min(value = 1, message = "pages must be at least 1")
    @field:Max(value = 10, message = "pages cannot be greater than 10")
    val pages: Int
)