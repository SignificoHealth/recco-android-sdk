package com.recco.internal.feature.bookmark

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.core.ui.preview.RecommendationPreviewProvider

internal class BookmarkUIPreviewProvider : PreviewParameterProvider<UiState<BookmarkUI>> {

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