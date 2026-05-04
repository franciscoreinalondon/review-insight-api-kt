package com.franciscoreina.reviewinsight.exceptions

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(EmptyReviewsException::class)
    fun handleEmptyReviewsException(ex: EmptyReviewsException): ResponseEntity<ApiErrorResponse> {
        logger.warn { "Review analysis failed: ${ex.message}" }

        return buildErrorResponse(
            HttpStatus.NOT_FOUND,
            code = "REVIEWS_NOT_FOUND",
            message = ex.message ?: "No reviews found for the requested app"
        )
    }

    @ExceptionHandler(ReviewProviderException::class)
    fun handleReviewProviderException(ex: ReviewProviderException): ResponseEntity<ApiErrorResponse> {
        logger.error { "External dependency failed: ${ex.message}" }

        return buildErrorResponse(
            HttpStatus.BAD_GATEWAY,
            code = "REVIEW_PROVIDER_ERROR",
            message = ex.message ?: "Review provider error"
        )
    }

    @ExceptionHandler(ReviewAnalyzerException::class)
    fun handleReviewAnalyzerException(ex: ReviewAnalyzerException): ResponseEntity<ApiErrorResponse> {
        logger.error { "External dependency failed: ${ex.message}" }

        return buildErrorResponse(
            HttpStatus.BAD_GATEWAY,
            code = "REVIEW_ANALYZER_ERROR",
            message = ex.message ?: "Review analyzer error"
        )
    }

    private fun buildErrorResponse(
        status: HttpStatus,
        code: String,
        message: String
    ): ResponseEntity<ApiErrorResponse> {
        val error = ApiErrorResponse(status.value(), code, message)
        return ResponseEntity.status(status).body(error)
    }
}