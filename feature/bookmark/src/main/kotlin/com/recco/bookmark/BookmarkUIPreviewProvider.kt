package com.recco.bookmark

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.recco.core.model.feed.FeedSectionState
import com.recco.core.model.feed.FeedSectionType
import com.recco.core.ui.components.UiState
import com.recco.core.ui.preview.FeedPreviewProvider
import com.recco.core.ui.preview.RecommendationPreviewProvider

class BookmarkUIPreviewProvider :
    PreviewParameterProvider<UiState<BookmarkUI>> {

    override val values
        get() = sequenceOf(
            UiState(
                isLoading = false,
                data = BookmarkUI(
                    recommendations = List(10) { RecommendationPreviewProvider.data }
                )
            ),
            UiState(isLoading = true),
            UiState(isLoading = false, error = Throwable())
        )
}