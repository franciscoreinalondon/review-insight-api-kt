package com.franciscoreina.reviewinsight.controller

import com.franciscoreina.reviewinsight.model.dto.InsightRequest
import com.franciscoreina.reviewinsight.model.dto.InsightResponse
import com.franciscoreina.reviewinsight.model.mapper.toResponse
import com.franciscoreina.reviewinsight.service.InsightService
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.Valid
import lombok.extern.slf4j.Slf4j
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
@RequestMapping("/v1/insight")
class InsightController(
    private val insightService: InsightService
) {

    private val logger = KotlinLogging.logger {}

    @PostMapping
    fun generateInsight(@Valid @RequestBody request: InsightRequest): ResponseEntity<InsightResponse> {
        logger.info { "Generating insight for app: ${request.appId} in ${request.country}" }

        val reviewInsight = insightService.generateInsight(request.appId, request.country, request.pages)

        return ResponseEntity.ok(reviewInsight.toResponse())
    }

}