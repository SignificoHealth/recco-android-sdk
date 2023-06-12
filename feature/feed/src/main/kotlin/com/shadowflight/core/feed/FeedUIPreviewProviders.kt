package com.shadowflight.core.feed

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
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
                            locked = false
                        ),
                        FeedPreviewProvider.data(
                            type = FeedSectionType.NUTRITION_EXPLORE,
                            locked = false
                        ),
                        FeedPreviewProvider.data(
                            type = FeedSectionType.SLEEP_RECOMMENDATIONS,
                            locked = true
                        )
                    )
                )
            ),
            UiState(isLoading = true),
            UiState(isLoading = false, error = Throwable())
        )
}