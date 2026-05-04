package com.franciscoreina.reviewinsight.controller

import com.franciscoreina.reviewinsight.exceptions.EmptyReviewsException
import com.franciscoreina.reviewinsight.exceptions.ReviewProviderException
import com.franciscoreina.reviewinsight.model.domain.*
import com.franciscoreina.reviewinsight.model.dto.InsightRequest
import com.franciscoreina.reviewinsight.service.InsightService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tools.jackson.databind.ObjectMapper
import java.time.OffsetDateTime
import kotlin.test.Test

@WebMvcTest(InsightController::class)
class ReviewAnalysisControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) {

    @MockkBean
    private lateinit var insightService: InsightService

    @Nested
    @DisplayName("POST /v1/insight")
    inner class GenerateReviewAnalysis {

        @Test
        fun `should return 200 OK and valid insight response`() {
            // GIVEN
            val request = createInsightRequest()
            val reviewInsight = createReviewInsight(
                reviews = listOf(createReview(sentiment = Sentiment.POSITIVE)),
                stats = ReviewStats(total = 1, positive = 1, neutral = 0, negative = 0)
            )

            every {
                insightService.generateInsight(
                    request.appId,
                    request.country,
                    request.pages
                )
            } returns reviewInsight

            // WHEN-THEN
            mockMvc.perform(
                post("/v1/insight")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.reviews.length()").value(1))
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.positive").value(1))
                .andExpect(
                    jsonPath("$.analysis.summary")
                        .value(reviewInsight.analysis.summary)
                )
                .andExpect(jsonPath("$.analysis.topProblems").isArray)

            verify(exactly = 1) { insightService.generateInsight(request.appId, request.country, request.pages) }
        }

        @Test
        fun `should return 404 Not Found when reviews list is empty`() {
            // GIVEN
            val request = createInsightRequest()
            val errorMessage = "Reviews cannot be empty"

            every {
                insightService.generateInsight(request.appId, request.country, request.pages)
            } throws EmptyReviewsException(errorMessage)

            // WHEN-THEN
            mockMvc.perform(
                post("/v1/insight")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("REVIEWS_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.timestamp").exists())

            verify(exactly = 1) { insightService.generateInsight(request.appId, request.country, request.pages) }
        }

        @Test
        fun `should return 502 Bad Gateway when provider do not respond`() {
            // GIVEN
            val request = createInsightRequest()
            val errorMessage = "Review provider error"

            every {
                insightService.generateInsight(request.appId, request.country, request.pages)
            } throws ReviewProviderException(errorMessage)

            // WHEN-THEN
            mockMvc.perform(
                post("/v1/insight")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isBadGateway)
                .andExpect(jsonPath("$.status").value(502))
                .andExpect(jsonPath("$.code").value("REVIEW_PROVIDER_ERROR"))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.timestamp").exists())

            verify(exactly = 1) { insightService.generateInsight(request.appId, request.country, request.pages) }
        }

    }

    // --- HELPERS ---

    private fun createInsightRequest(
        appId: Int = 123,
        country: String = "gb",
        pages: Int = 1
    ): InsightRequest {
        return InsightRequest(appId, country, pages)
    }

    private fun createReviewInsight(
        reviews: List<Review> = listOf(createReview()),
        stats: ReviewStats = createReviewStats(),
        analysis: ReviewAnalysis = createReviewAnalysis()
    ): ReviewInsight {
        return ReviewInsight(reviews, stats, analysis)
    }

    private fun createReviewStats(): ReviewStats {
        return ReviewStats(1, 1, 0, 0)
    }

    private fun createReview(
        author: String = "author",
        rating: Int = 5,
        sentiment: Sentiment = Sentiment.POSITIVE
    ): Review {
        return Review(
            author = author,
            rating = rating,
            title = "title",
            content = "content",
            voteCount = 0,
            date = OffsetDateTime.now(),
            sentiment = sentiment
        )
    }

    private fun createReviewAnalysis(
        summary: String = "default summary",
        topProblems: List<Problem> = listOf(createProblem())
    ): ReviewAnalysis {
        return ReviewAnalysis(summary, topProblems)
    }

    private fun createProblem(
        name: String = "default problem",
        count: Int = 1,
        examples: List<String> = listOf("example")
    ): Problem {
        return Problem(name, count, examples)
    }
}