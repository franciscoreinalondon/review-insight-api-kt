package com.franciscoreina.reviewinsight.client.apple.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class AppleRssResponseDTO(val feed: AppleFeedResponseDTO)

@JsonIgnoreProperties(ignoreUnknown = true)
data class AppleFeedResponseDTO(@JsonProperty(value = "entry") val entries: List<AppleReviewDTO> = emptyList())

@JsonIgnoreProperties(ignoreUnknown = true)
data class AppleReviewDTO(
    val author: AuthorDTO,
    val updated: LabelDTO,
    @JsonProperty(value = "im:rating")
    val rating: LabelDTO,
    val title: LabelDTO,
    val content: LabelDTO,
    @JsonProperty(value = "im:voteCount")
    val voteCount: LabelDTO
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class AuthorDTO(val name: LabelDTO)

@JsonIgnoreProperties(ignoreUnknown = true)
data class LabelDTO(val label: String)