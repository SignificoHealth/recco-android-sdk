package com.recco.bookmark

import com.recco.core.model.recommendation.Recommendation

data class BookmarkUI(
    val recommendations: List<Recommendation> = emptyList()
)