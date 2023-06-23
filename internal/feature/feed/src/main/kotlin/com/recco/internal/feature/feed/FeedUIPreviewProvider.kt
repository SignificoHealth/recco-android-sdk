package com.recco.internal.feature.feed

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.recco.internal.core.model.feed.FeedSectionState
import com.recco.internal.core.model.feed.FeedSectionType
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.core.ui.preview.FeedPreviewProvider

class FeedUIPreviewProvider :
    PreviewParameterProvider<UiState<FeedUI>> {

    override val values
        get() = sequenceOf(
            UiState(
                isLoading = false,
                data = FeedUI(
                    sections = listOf(
                        FeedPreviewProvider.data(
                            type = FeedSectionType.PHYSICAL_ACTIVITY_RECOMMENDATIONS,
                            state = FeedSectionState.UNLOCKED
                        ),
                        FeedPreviewProvider.data(
                            type = FeedSectionType.SLEEP_RECOMMENDATIONS,
                            state = FeedSectionState.PARTIALLY_UNLOCKED,
                            recommendationsSize = 1
                        ),
                        FeedPreviewProvider.data(
                            type = FeedSectionType.NUTRITION_EXPLORE,
                            state = FeedSectionState.UNLOCKED
                        ),
                        FeedPreviewProvider.data(
                            type = FeedSectionType.SLEEP_RECOMMENDATIONS,
                            state = FeedSectionState.UNLOCKED
                        )
                    )
                )
            ),
            UiState(isLoading = true),
            UiState(isLoading = false, error = Throwable())
        )
}