package com.franciscoreina.reviewinsight.client.openai.mock

import com.franciscoreina.reviewinsight.client.ReviewAnalyzer
import com.franciscoreina.reviewinsight.model.domain.Review
import com.franciscoreina.reviewinsight.model.domain.ReviewAnalysis
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import tools.jackson.databind.json.JsonMapper

@Component
@ConditionalOnProperty(
    name = ["app.mock-mode"],
    havingValue = "true", // Activates mock implementation
)
class MockAnalyzerProvider(private val jsonMapper: JsonMapper) : ReviewAnalyzer {

    override fun analyze(reviews: List<Review>): ReviewAnalysis {
        val jsonStream = ClassPathResource("mocks/openai/zopa-analysis.json").inputStream
        return jsonMapper.readValue(jsonStream, ReviewAnalysis::class.java)
    }

}