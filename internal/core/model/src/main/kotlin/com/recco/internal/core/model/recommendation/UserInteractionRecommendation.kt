package com.recco.internal.core.model.recommendation

data class UserInteractionRecommendation(
    val contentId: ContentId,
    val contentType: ContentType,
    val rating: Rating,
    val isBookmarked: Boolean = false,
    val isBookmarkLoading: Boolean = false,
    val isLikeLoading: Boolean = false,
    val isDislikeLoading: Boolean = false
)
