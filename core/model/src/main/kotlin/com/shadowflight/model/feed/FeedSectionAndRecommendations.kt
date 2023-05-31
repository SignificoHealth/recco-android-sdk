package com.shadowflight.model.feed

import com.shadowflight.model.recommendation.Recommendation

data class FeedSectionAndRecommendations(
    val feedSection: FeedSection,
    val recommendations: List<Recommendation>
)