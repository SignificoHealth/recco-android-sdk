package com.shadowflight.core.model.feed

import com.shadowflight.core.model.recommendation.Recommendation

data class FeedSectionAndRecommendations(
    val feedSection: FeedSection,
    val recommendations: List<Recommendation>
)