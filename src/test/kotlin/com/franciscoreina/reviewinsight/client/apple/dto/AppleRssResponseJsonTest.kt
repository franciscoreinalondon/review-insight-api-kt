package com.franciscoreina.reviewinsight.client.apple.dto

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.core.io.ClassPathResource
import kotlin.test.Test

@JsonTest
class AppleRssResponseJsonTest @Autowired constructor(
    private val json: JacksonTester<AppleRssResponseDTO>
) {

    @Test
    fun `should parse full apple rss json correctly`() {
        // GIVEN
        val jsonPath = ClassPathResource("mocks/apple/zopa-reviews.json")

        // WHEN
        val result = json.read(jsonPath).getObject()

        // THEN
        assertThat(result.feed.entries).isNotEmpty

        val review = result.feed.entries.first()
        assertThat(review.author.name.label).isEqualTo("Simon Theaker")
        assertThat(review.rating.label).isEqualTo("5")
        assertThat(review.title.label).isEqualTo("Fantastic credit card")
        assertThat(review.content.label).startsWith("Great credit builder credit card")
        assertThat(review.voteCount.label).isEqualTo("0")
        assertThat(review.updated.label).isEqualTo("2026-03-03T08:03:35-07:00")
    }
}