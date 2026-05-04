package com.franciscoreina.reviewinsight.client.apple

import com.franciscoreina.reviewinsight.client.apple.dto.*
import io.mockk.coVerify
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class AppleReviewProviderTest() {

    private val rssClient = mockk<AppleRssClient>()
    private val provider = AppleReviewProvider(rssClient)

    private val appId = 12345
    private val country = "gb"

    @Nested
    @DisplayName("Happy Path")
    inner class HappyPath {

        @Test
        fun `should fetch and map reviews for multiple pages`() {
            // GIVEN
            val responsePage1 = createAppleRssResponseDTO(author = "User 1")
            val responsePage2 = createAppleRssResponseDTO(author = "User 2")

            every { rssClient.fetchReviews(appId, country, 1) } returns responsePage1
            every { rssClient.fetchReviews(appId, country, 2) } returns responsePage2

            // WHEN
            val result = provider.fetchReviews(appId, country, 2)

            // THEN
            assertThat(result).hasSize(2)
            assertThat(result[0].author).isEqualTo("User 1")
            assertThat(result[1].author).isEqualTo("User 2")

            coVerify(exactly = 1) { rssClient.fetchReviews(appId, country, 1) }
            coVerify(exactly = 1) { rssClient.fetchReviews(appId, country, 2) }
        }

    }

    // --- HELPERS ---

    private fun createAppleRssResponseDTO(
        author: String
    ): AppleRssResponseDTO {
        return AppleRssResponseDTO(feed = createAppleFeedResponseDTO(author))
    }

    private fun createAppleFeedResponseDTO(
        author: String
    ): AppleFeedResponseDTO {
        return AppleFeedResponseDTO(entries = listOf(createAppleReviewDTO(author)))
    }

    private fun createAppleReviewDTO(
        author: String = "default author"
    ): AppleReviewDTO {
        return AppleReviewDTO(
            author = AuthorDTO(LabelDTO(author)),
            updated = LabelDTO("2026-05-01T12:00:00-07:00"),
            rating = LabelDTO("5"),
            title = LabelDTO("default title"),
            content = LabelDTO("default content"),
            voteCount = LabelDTO("0")
        )
    }

}