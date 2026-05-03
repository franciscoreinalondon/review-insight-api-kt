package com.franciscoreina.reviewinsight.service

import com.franciscoreina.reviewinsight.model.domain.ReviewInsight

interface InsightService {

    /**
     * Orchestrates the entire workflow: obtaining reviews, calculating statistics,
     * and generating the analysis using AI.
     *
     * @param appId The app's identifier.
     * @param country The country code.
     * @param pages Number of review pages to retrieve (50 per page).
     * @return A [ReviewInsight] object containing the complete report.
     */

    fun generateInsight(appId: Int, country: String, pages: Int): ReviewInsight

}