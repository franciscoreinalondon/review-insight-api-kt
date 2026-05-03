package com.franciscoreina.reviewinsight.service

import com.franciscoreina.reviewinsight.client.ReviewAnalyzer
import com.franciscoreina.reviewinsight.exceptions.AiAnalysisException
import com.franciscoreina.reviewinsight.exceptions.EmptyReviewsException
import com.franciscoreina.reviewinsight.model.domain.Insight
import com.franciscoreina.reviewinsight.model.domain.Problem
import com.franciscoreina.reviewinsight.model.domain.Review
import com.franciscoreina.reviewinsight.model.domain.Sentiment
import io.mockk.Called
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.OffsetDateTime

@ExtendWith(MockKExtension::class)
class InsightServiceImplTest {

    private val reviewAnalyzerMock = mockk<ReviewAnalyzer>()
    private val insightService = InsightServiceImpl(reviewAnalyzerMock)

    @Nested
    @DisplayName("Successful generation")
    inner class HappyPath {

        @Test
        fun `should generate insight from reviews`() {
            // GIVEN
            val reviews = listOf(createReview())
            val problems = listOf(createProblem())
            val insight = createInsight("Success", problems)

            every { reviewAnalyzerMock.analyze(reviews) } returns insight

            // WHEN
            val result = insightService.generateInsight(reviews)

            // THEN
            assertThat(result).isEqualTo(insight)
            verify(exactly = 1) { reviewAnalyzerMock.analyze(reviews) }
        }

    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    inner class EdgeCases {

        @Test
        fun `should throw exception when reviews is empty`() {
            // GIVEN
            val reviews = emptyList<Review>()
            val error = "Reviews cannot be empty"

            // WHEN-THEN
            assertThatThrownBy {
                insightService.generateInsight(reviews)
            }
                .isInstanceOf(EmptyReviewsException::class.java)
                .hasMessage(error)

            verify { reviewAnalyzerMock wasNot Called }
        }

        @Test
        fun `should throw exception when review analyzer fails`() {
            // GIVEN
            val reviews = listOf(createReview())
            val error = "Review Analyzer error"
            val originalCause = RuntimeException("Connection timeout")

            every { reviewAnalyzerMock.analyze(reviews) } throws AiAnalysisException(error, originalCause)

            // WHEN-THEN
            assertThatThrownBy {
                insightService.generateInsight(reviews)
            }
                .isInstanceOf(AiAnalysisException::class.java)
                .hasMessage(error)
                .hasCause(originalCause)

            verify(exactly = 1) { reviewAnalyzerMock.analyze(reviews) }
        }

    }

    // --- HELPERS ---

    private fun createReview(
        author: String = "author",
        rating: Int = 5,
        title: String = "title",
        content: String = "content",
        voteCount: Int = 0,
        date: OffsetDateTime = OffsetDateTime.now(),
        sentiment: Sentiment = Sentiment.POSITIVE
    ): Review {
        return Review(
            author = author,
            rating = rating,
            title = title,
            content = content,
            voteCount = voteCount,
            date = date,
            sentiment = sentiment
        )
    }

    private fun createProblem(
        name: String = "default problem", count: Int = 0, examples: List<String> = emptyList()
    ): Problem {
        return Problem(name, count, examples)
    }

    private fun createInsight(
        summary: String, topProblems: List<Problem>
    ): Insight {
        return Insight(summary, topProblems)
    }
}