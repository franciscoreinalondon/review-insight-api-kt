package com.franciscoreina.reviewinsight.client.openai.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenAiRequestDTO(
    val model: String,
    val input: List<OpenAiMessageDTO>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpenAiMessageDTO(
    val role: String,
    val content: String
)