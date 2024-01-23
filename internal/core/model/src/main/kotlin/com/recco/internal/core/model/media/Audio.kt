package com.recco.internal.core.model.media

import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.Status

data class Audio(
    val id: ContentId,
    val rating: Rating,
    val status: Status,
    val isBookmarked: Boolean,
    val audioUrl: String,
    val headline: String,
    val imageUrl: String? = null,
    val imageAlt: String? = null,
    val length: Int? = null
)
