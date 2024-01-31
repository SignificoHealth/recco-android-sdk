package com.recco.internal.core.ui.preview

import com.recco.internal.core.model.FlowDataState
import com.recco.internal.core.model.feed.FeedSection
import com.recco.internal.core.model.feed.FeedSectionAndRecommendations
import com.recco.internal.core.model.feed.FeedSectionState
import com.recco.internal.core.model.feed.FeedSectionType
import com.recco.internal.core.model.feed.Topic

class FeedPreviewProvider {

    companion object {
        fun data(
            type: FeedSectionType,
            state: FeedSectionState = FeedSectionState.LOCKED,
            recommendationsSize: Int = 10,
            isRecommendationsLoading: Boolean = false
        ) = FeedSectionAndRecommendations(
            recommendations = if (isRecommendationsLoading) {
                FlowDataState.Loading
            } else {
                FlowDataState.Success(List(recommendationsSize) { RecommendationPreviewProvider.ARTICLE })
            },
            feedSection = FeedSection(
                type = type,
                state = state,
                topic = Topic.PHYSICAL_ACTIVITY
            )
        )
    }
}
