package com.recco.internal.feature.bookmark.model

import com.recco.internal.core.ui.components.UiState
import com.recco.internal.core.ui.preview.RecommendationPreviewProvider
import com.recco.internal.feature.bookmark.BookmarkUI

internal val recommendation = RecommendationPreviewProvider.data
internal val bookmarkUI = BookmarkUI(recommendations = listOf(recommendation))

/**
 * Expected UiState
 */
internal val expectedWithData = UiState(
    isLoading = false,
    data = bookmarkUI,
    error = null
)

internal val expectedWithLoading = UiState(
    isLoading = true,
    data = null,
    error = null
)

internal val staticThrowableForTesting = IllegalStateException()
internal val expectedWithError = UiState(
    isLoading = false,
    data = null,
    error = staticThrowableForTesting
)
