package com.franciscoreina.reviewinsight.client.apple.mapper

import com.franciscoreina.reviewinsight.client.apple.dto.AppleReviewDTO
import com.franciscoreina.reviewinsight.client.apple.dto.AuthorDTO
import com.franciscoreina.reviewinsight.client.apple.dto.LabelDTO
import com.franciscoreina.reviewinsight.model.domain.Sentiment
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.time.OffsetDateTime
import kotlin.test.Test

class ReviewMapperTest {

    @Test
    fun `should map basic strings from apple dto to domain`() {
        // GIVEN
        val dto = createAppleReviewDTO(author = "Francisco", title = "Amazing App", content = "It's so great")

        // WHEN
        val review = dto.toDomain()

        // THEN
        assertThat(review.author).isEqualTo("Francisco")
        assertThat(review.title).isEqualTo("Amazing App")
        assertThat(review.content).isEqualTo("It's so great")
    }

    @Test
    fun `should map strings to integers for rating and vote`() {
        // GIVEN
        val dto = createAppleReviewDTO(rating = "5", voteCount = "10")

        // WHEN
        val review = dto.toDomain()

        // THEN
        assertThat(review.rating).isEqualTo(5)
        assertThat(review.voteCount).isEqualTo(10)
    }

    @Test
    fun `should parse updated date string to offsetdatetime`() {
        // GIVEN
        val dto = createAppleReviewDTO(updated = "2026-05-01T10:50:00-07:00")

        // WHEN
        val review = dto.toDomain()

        // THEN
        assertThat(review.date).isEqualTo(OffsetDateTime.parse("2026-05-01T10:50:00-07:00"))
    }

    @ParameterizedTest
    @CsvSource(
        "5, POSITIVE",
        "4, POSITIVE",
        "3, NEUTRAL",
        "2, NEGATIVE",
        "1, NEGATIVE",
        "0, NEGATIVE"
    )
    fun `should assign sentiment from numeric rating`(rating: String, expectedSentiment: Sentiment) {
        // GIVEN
        val dto = createAppleReviewDTO(rating = rating)

        // WHEN
        val review = dto.toDomain()

        // THEN
        assertThat(review.sentiment).isEqualTo(expectedSentiment)
    }

    @Test
    fun `should handle invalid numeric string`() {
        // GIVEN
        val dto = createAppleReviewDTO(rating = "N/A", voteCount = "")

        // WHEN
        val review = dto.toDomain()

        // THEN
        assertThat(review.rating).isEqualTo(0)
        assertThat(review.voteCount).isEqualTo(0)
        assertThat(review.sentiment).isEqualTo(Sentiment.UNKNOWN)
    }

    // --- HELPERS ---

    private fun createAppleReviewDTO(
        author: String = "author",
        updated: String = "2026-04-28T11:52:49-07:00",
        rating: String = "0",
        title: String = "title",
        content: String = "content",
        voteCount: String = "0"
    ): AppleReviewDTO {
        return AppleReviewDTO(
            author = AuthorDTO(LabelDTO(author)),
            updated = LabelDTO(updated),
            rating = LabelDTO(rating),
            title = LabelDTO(title),
            content = LabelDTO(content),
            voteCount = LabelDTO(voteCount)
        )
    }
}