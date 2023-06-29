package com.recco.internal.core.model.feed

import com.recco.internal.core.model.LoadingStateIn
import com.recco.internal.core.model.recommendation.Recommendation

data class FeedSectionAndRecommendations(
    val feedSection: FeedSection,
    val recommendations: LoadingStateIn<List<Recommendation>>
)