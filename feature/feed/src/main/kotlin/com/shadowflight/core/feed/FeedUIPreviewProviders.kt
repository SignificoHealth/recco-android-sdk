package com.shadowflight.core.feed

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.shadowflight.core.model.feed.FeedSectionState
import com.shadowflight.core.model.feed.FeedSectionType
import com.shadowflight.core.ui.components.UiState
import com.shadowflight.core.ui.preview.FeedPreviewProvider

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
                            state = FeedSectionState.UNLOCK
                        ),
                        FeedPreviewProvider.data(
                            type = FeedSectionType.SLEEP_RECOMMENDATIONS,
                            state = FeedSectionState.PARTIALLY_UNLOCK,
                            recommendationsSize = 1
                        ),
                        FeedPreviewProvider.data(
                            type = FeedSectionType.NUTRITION_EXPLORE,
                            state = FeedSectionState.UNLOCK
                        ),
                        FeedPreviewProvider.data(
                            type = FeedSectionType.SLEEP_RECOMMENDATIONS,
                            state = FeedSectionState.UNLOCK
                        )
                    )
                )
            ),
            UiState(isLoading = true),
            UiState(isLoading = false, error = Throwable())
        )
}