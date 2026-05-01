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
        val author = "Author name"
        val title = "Stress Free Borrowing"
        val content = "I have had a number of loans..."
        val dto = createAppleReviewDTO(author, title, content)

        // WHEN
        val review = dto.toDomain()

        // THEN
        assertThat(review.author).isEqualTo(author)
        assertThat(review.title).isEqualTo(title)
        assertThat(review.content).isEqualTo(content)
    }

    // --- HELPERS ---

    private fun createAppleReviewDTO(author: String, title: String, content: String): AppleReviewDTO {
        return AppleReviewDTO(
            author = AuthorDTO(LabelDTO(author)),
            updated = LabelDTO("2026-04-28T11:52:49-07:00"),
            rating = LabelDTO("5"),
            title = LabelDTO(title),
            content = LabelDTO(content),
            voteCount = LabelDTO("0")
        )
    }
}