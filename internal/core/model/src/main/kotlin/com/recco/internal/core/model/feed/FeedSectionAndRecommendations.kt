package com.recco.internal.core.model.feed

import com.recco.internal.core.model.recommendation.Recommendation

data class FeedSectionAndRecommendations(
    val feedSection: FeedSection,
    val recommendations: List<Recommendation>
)