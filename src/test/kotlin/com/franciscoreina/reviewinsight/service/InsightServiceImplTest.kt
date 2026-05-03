package com.franciscoreina.reviewinsight.service

import com.franciscoreina.reviewinsight.client.ReviewAnalyzer
import com.franciscoreina.reviewinsight.client.ReviewProvider
import com.franciscoreina.reviewinsight.exceptions.AiAnalysisException
import com.franciscoreina.reviewinsight.exceptions.EmptyReviewsException
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

    private val reviewProviderMock = mockk<ReviewProvider>()
    private val reviewAnalyzerMock = mockk<ReviewAnalyzer>()
    private val insightService = InsightServiceImpl(reviewProviderMock, reviewAnalyzerMock)

    @Nested
    @DisplayName("Happy Path")
    inner class HappyPath {

        @Test
        fun `should fetch reviews and generate full review insight report`() {
            // GIVEN
            val appId = 12345
            val country = "gb"
            val pages = 1
            val reviews = listOf(
                createReview(sentiment = Sentiment.POSITIVE),
                createReview(sentiment = Sentiment.POSITIVE),
                createReview(sentiment = Sentiment.NEGATIVE)
            )
            val analysis = ReviewAnalysis(summary = "Generally positive", topProblems = emptyList())

            every { reviewProviderMock.fetchReviews(appId, country, pages) } returns reviews
            every { reviewAnalyzerMock.analyze(reviews) } returns analysis

            // WHEN
            val result = insightService.generateInsight(appId, country, pages)

            // THEN
            assertThat(result.reviews).isEqualTo(reviews)
            assertThat(result.stats.total).isEqualTo(3)
            assertThat(result.stats.positive).isEqualTo(2)
            assertThat(result.stats.negative).isEqualTo(1)
            assertThat(result.analysis).isEqualTo(analysis)

            verify(exactly = 1) { reviewProviderMock.fetchReviews(appId, country, pages) }
            verify(exactly = 1) { reviewAnalyzerMock.analyze(reviews) }
        }

    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    inner class EdgeCases {

        @Test
        fun `should throw exception when reviews list is empty`() {
            // GIVEN
            val appId = 12345
            val country = "gb"
            val pages = 1

            every { reviewProviderMock.fetchReviews(appId, country, pages) } returns emptyList()

            // WHEN-THEN
            assertThatThrownBy {
                insightService.generateInsight(appId, country, pages)
            }
                .isInstanceOf(EmptyReviewsException::class.java)
                .hasMessage("Reviews cannot be empty for app $appId")

            verify { reviewAnalyzerMock wasNot Called }
        }

        @Test
        fun `should throw propagate exception when review analyzer fails`() {
            // GIVEN
            val appId = 12345
            val country = "gb"
            val pages = 1
            val reviews = listOf(createReview())
            val error = "Review Analyzer error"
            val originalCause = RuntimeException("Connection timeout")

            every { reviewProviderMock.fetchReviews(appId, country, pages) } returns reviews
            every { reviewAnalyzerMock.analyze(reviews) } throws AiAnalysisException(error, originalCause)

            // WHEN-THEN
            assertThatThrownBy {
                insightService.generateInsight(appId, country, pages)
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
}