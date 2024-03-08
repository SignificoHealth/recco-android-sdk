package com.recco.internal.core.ui.preview

import com.recco.internal.core.model.FlowDataState
import com.recco.internal.core.model.feed.FeedSection
import com.recco.internal.core.model.feed.FeedSectionAndRecommendations
import com.recco.internal.core.model.feed.FeedSectionState
import com.recco.internal.core.model.feed.FeedSectionType
import com.recco.internal.core.model.feed.Topic
import com.recco.internal.core.model.recommendation.ContentId

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
                FlowDataState.Success(
                    List(recommendationsSize) { index ->
                        RecommendationPreviewProvider.ARTICLE.copy(
                            // Sometimes generating random IDs from previews
                            // doesn't make them random. Making lLazyList's keys to fail
                            id = ContentId(index.toString(), index.toString())
                        )
                    }
                )
            },
            feedSection = FeedSection(
                type = type,
                state = state,
                topic = Topic.PHYSICAL_ACTIVITY
            )
        )
    }
}
