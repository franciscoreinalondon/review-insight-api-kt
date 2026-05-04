package com.franciscoreina.reviewinsight.client.openai

import com.franciscoreina.reviewinsight.client.ReviewAnalyzer
import com.franciscoreina.reviewinsight.client.openai.dto.OpenAiMessageDTO
import com.franciscoreina.reviewinsight.client.openai.dto.OpenAiRequestDTO
import com.franciscoreina.reviewinsight.exceptions.ReviewAnalyzerException
import com.franciscoreina.reviewinsight.model.domain.Review
import com.franciscoreina.reviewinsight.model.domain.ReviewAnalysis
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import tools.jackson.databind.json.JsonMapper

@Component
@ConditionalOnProperty(name = ["app.mock-mode"], havingValue = "false") // If mock disabled -> enables real API call
class OpenAiReviewAnalyzer(
    private val openAiClient: OpenAiClient,
    private val jsonMapper: JsonMapper
) : ReviewAnalyzer {

    override fun analyze(reviews: List<Review>): ReviewAnalysis {
        val prompt = buildPrompt(reviews)

        val request = OpenAiRequestDTO(
            model = "gpt-4.1-mini",
            input = listOf(OpenAiMessageDTO(role = "user", content = prompt))
        )

        val rawResponse = openAiClient.postAnalysis(request)
        return parseResponse(rawResponse)
    }

    private fun buildPrompt(reviews: List<Review>): String {
        val reviewsText = reviews.joinToString(separator = "\n") { "- ${it.content}" }

        return """
            You are analyzing app reviews.
    
            TASK:
            Identify the top 3 most common problems mentioned by users.
    
            RULES:
            - Group similar issues together (e.g. login issues, crashes, support problems)
            - Count approximate occurrences
            - Be concise
            - Ignore positive feedback
            - Focus only on problems
    
            OUTPUT FORMAT (STRICT):
            Return ONLY a valid JSON. No explanations, no extra text.
    
            JSON schema:
            {
              "summary": "string",
              "topProblems": [
                {
                  "name": "string",
                  "count": number,
                  "examples": ["string", "string"]
                }
              ]
            }
    
            REQUIREMENTS:
            - summary must be a short readable paragraph
            - topProblems must contain EXACTLY 3 items
            - count must be an integer
            - examples must contain 2-3 short phrases from reviews
            - DO NOT include markdown
            - DO NOT include backticks
            - DO NOT include any text outside JSON
    
            REVIEWS:
            $reviewsText
        """.trimIndent()
    }

    private fun parseResponse(response: String): ReviewAnalysis {
        return try {
            val root = jsonMapper.readTree(response)

            val jsonText = root.path("output")
                .get(0)
                .path("content")
                .get(0)
                .path("text")
                .asText()

            jsonMapper.readValue(jsonText, ReviewAnalysis::class.java)
        } catch (ex: Exception) {
            throw ReviewAnalyzerException("Failed to parse the structured JSON from OpenAI response", ex)
        }
    }
}