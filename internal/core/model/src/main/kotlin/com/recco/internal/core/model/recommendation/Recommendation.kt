package com.recco.internal.core.model.recommendation

data class Recommendation(
    val id: ContentId,
    val type: ContentType,
    val rating: Rating,
    val status: Status,
    val bookmarked: Boolean,
    val headline: String,
    val imageUrl: String? = null,
    val imageAlt: String? = null,
    val lengthInSeconds: Int? = null
)
