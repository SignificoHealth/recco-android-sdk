package com.recco.internal.core.model.feed

import com.recco.internal.core.model.FlowDataState
import com.recco.internal.core.model.recommendation.Recommendation

data class FeedSectionAndRecommendations(
    val feedSection: FeedSection,
    val recommendations: FlowDataState<List<Recommendation>>
)
