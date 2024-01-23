package com.recco.internal.core.model.media

import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.Status

data class Video(
    val id: ContentId,
    val rating: Rating,
    val status: Status,
    val isBookmarked: Boolean,
    val videoUrl: String,
    val headline: String,
    val description: String? = null,
    val lead: String? = null,
    val imageUrl: String? = null,
    val imageAlt: String? = null,
    val length: kotlin.Int? = null
)
