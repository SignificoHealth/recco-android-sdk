package com.recco.internal.feature.bookmark

import com.recco.internal.core.model.recommendation.Recommendation

data class BookmarkUI(
    val recommendations: List<Recommendation> = emptyList()
)