package com.franciscoreina.reviewinsight.client.apple.mapper

import com.franciscoreina.reviewinsight.client.apple.dto.AppleReviewDTO
import com.franciscoreina.reviewinsight.client.apple.dto.AuthorDTO
import com.franciscoreina.reviewinsight.client.apple.dto.LabelDTO
import org.assertj.core.api.Assertions.assertThat
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

    // --- HELPERS ---

    private fun createAppleReviewDTO(
        author: String = "author",
        rating: String = "0",
        title: String = "title",
        content: String = "content",
        voteCount: String = "0"
    ): AppleReviewDTO {
        return AppleReviewDTO(
            author = AuthorDTO(LabelDTO(author)),
            updated = LabelDTO("2026-04-28T11:52:49-07:00"),
            rating = LabelDTO(rating),
            title = LabelDTO(title),
            content = LabelDTO(content),
            voteCount = LabelDTO(voteCount)
        )
    }
}