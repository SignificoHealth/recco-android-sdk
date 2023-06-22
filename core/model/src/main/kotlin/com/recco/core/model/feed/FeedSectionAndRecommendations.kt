package com.recco.core.model.feed

import com.recco.core.model.recommendation.Recommendation

data class FeedSectionAndRecommendations(
    val feedSection: FeedSection,
    val recommendations: List<Recommendation>
)