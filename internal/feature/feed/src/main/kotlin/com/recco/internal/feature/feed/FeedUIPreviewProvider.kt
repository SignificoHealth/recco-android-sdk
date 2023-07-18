package com.recco.internal.feature.feed

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.recco.internal.core.model.feed.FeedSectionState
import com.recco.internal.core.model.feed.FeedSectionType
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.core.ui.preview.FeedPreviewProvider

internal class FeedUIPreviewProvider :
    PreviewParameterProvider<UiState<FeedUI>> {

    override val values
        get() = sequenceOf(
            UiState(
                isLoading = false,
                data = data()
            ),
            UiState(isLoading = true),
            UiState(isLoading = false, error = Throwable())
        )

    companion object {
        fun data() = FeedUI(
            sections = listOf(
                FeedPreviewProvider.data(
                    type = FeedSectionType.PHYSICAL_ACTIVITY_RECOMMENDATIONS,
                    state = FeedSectionState.UNLOCKED
                ),
                FeedPreviewProvider.data(
                    type = FeedSectionType.PHYSICAL_ACTIVITY_RECOMMENDATIONS,
                    state = FeedSectionState.LOCKED,
                    recommendationsSize = 1
                ),
                FeedPreviewProvider.data(
                    type = FeedSectionType.SLEEP_RECOMMENDATIONS,
                    state = FeedSectionState.PARTIALLY_UNLOCKED,
                    recommendationsSize = 1
                )
            )
        )
    }
}
