package com.franciscoreina.reviewinsight.service

import com.franciscoreina.reviewinsight.client.ReviewAnalyzer
import com.franciscoreina.reviewinsight.client.ReviewProvider
import com.franciscoreina.reviewinsight.exceptions.AiAnalysisException
import com.franciscoreina.reviewinsight.exceptions.EmptyReviewsException
import com.franciscoreina.reviewinsight.exceptions.ReviewProviderException
import com.franciscoreina.reviewinsight.model.domain.Review
import com.franciscoreina.reviewinsight.model.domain.ReviewAnalysis
import com.franciscoreina.reviewinsight.model.domain.Sentiment
import io.mockk.Called
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.OffsetDateTime

@ExtendWith(MockKExtension::class)
class InsightServiceImplTest {

    private val reviewProvider = mockk<ReviewProvider>()
    private val reviewAnalyzer = mockk<ReviewAnalyzer>()
    private val insightService = InsightServiceImpl(reviewProvider, reviewAnalyzer)

    private val appId = 12345
    private val country = "gb"
    private val pages = 1

    @Nested
    @DisplayName("Happy Path")
    inner class HappyPath {

        @Test
        fun `should fetch reviews and generate full review insight report`() {
            // GIVEN
            val reviews = listOf(
                createReview(sentiment = Sentiment.POSITIVE),
                createReview(sentiment = Sentiment.POSITIVE),
                createReview(sentiment = Sentiment.NEGATIVE)
            )
            val analysis = ReviewAnalysis(summary = "Generally positive", topProblems = emptyList())

            every { reviewProvider.fetchReviews(appId, country, pages) } returns reviews
            every { reviewAnalyzer.analyze(reviews) } returns analysis

            // WHEN
            val result = insightService.generateInsight(appId, country, pages)

            // THEN
            assertThat(result.reviews).hasSize(3).isEqualTo(reviews)

            with(result.stats) {
                assertThat(total).isEqualTo(3)
                assertThat(positive).isEqualTo(2)
                assertThat(negative).isEqualTo(1)
                assertThat(neutral).isEqualTo(0)
            }

            assertThat(result.analysis).isEqualTo(analysis)

            verify(exactly = 1) { reviewProvider.fetchReviews(appId, country, pages) }
            verify(exactly = 1) { reviewAnalyzer.analyze(reviews) }
        }

    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    inner class EdgeCases {

        @Test
        fun `should throw exception when reviews list is empty`() {
            // GIVEN
            every { reviewProvider.fetchReviews(appId, country, pages) } returns emptyList()

            // WHEN-THEN
            assertThatThrownBy {
                insightService.generateInsight(appId, country, pages)
            }
                .isInstanceOf(EmptyReviewsException::class.java)
                .hasMessage("Reviews cannot be empty for app $appId")

            verify(exactly = 1) { reviewProvider.fetchReviews(appId, country, pages) }
            verify { reviewAnalyzer wasNot Called }
        }

        @Test
        fun `should throw propagate exception when review provider fails`() {
            // GIVEN
            val errorMessage = "Review Provider Error"
            val cause = RuntimeException("Connection timeout")

            every { reviewProvider.fetchReviews(appId, country, pages) } throws
                    ReviewProviderException(errorMessage, cause)

            // WHEN-THEN
            assertThatThrownBy {
                insightService.generateInsight(appId, country, pages)
            }
                .isInstanceOf(ReviewProviderException::class.java)
                .hasMessage(errorMessage)
                .hasCause(cause)

            verify(exactly = 1) { reviewProvider.fetchReviews(appId, country, pages) }
            verify { reviewAnalyzer wasNot Called }
        }

        @Test
        fun `should throw propagate exception when review analyzer fails`() {
            // GIVEN
            val reviews = listOf(createReview())
            val errorMessage = "Review Analyzer Error"
            val cause = RuntimeException("Connection timeout")

            every { reviewProvider.fetchReviews(appId, country, pages) } returns reviews
            every { reviewAnalyzer.analyze(reviews) } throws AiAnalysisException(errorMessage, cause)

            // WHEN-THEN
            assertThatThrownBy {
                insightService.generateInsight(appId, country, pages)
            }
                .isInstanceOf(AiAnalysisException::class.java)
                .hasMessage(errorMessage)
                .hasCause(cause)

            verify(exactly = 1) { reviewAnalyzer.analyze(reviews) }
        }

    }

    // --- HELPERS ---

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
}