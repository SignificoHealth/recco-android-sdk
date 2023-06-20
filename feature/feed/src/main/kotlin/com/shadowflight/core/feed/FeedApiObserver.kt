package com.shadowflight.core.feed

import com.shadowflight.core.model.feed.FeedSectionType
import com.shadowflight.core.repository.FeedRepository
import com.shadowflight.core.repository.RecommendationRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class FeedApiObserver @Inject constructor(
    private val feedRepository: FeedRepository,
    private val recommendationRepository: RecommendationRepository,
) {
    private var previousFeedSectionId = 0
    private var previousFeedRecommendationsId = 0
    private var currentFeedSectionId = 0
    private var currentFeedRecommendationsId = 0

    fun hasFinished(feedSectionType: FeedSectionType?): Boolean {
        currentFeedSectionId = feedRepository.feedSectionsPipelineId
        currentFeedRecommendationsId = recommendationRepository.getPipelineId(feedSectionType)

        return (currentFeedRecommendationsId != 0
                && previousFeedSectionId != currentFeedSectionId
                && previousFeedRecommendationsId != currentFeedRecommendationsId).also {
            if (it) {
                reset()
            }
        }
    }

    fun reset() {
        previousFeedSectionId = currentFeedSectionId
        previousFeedRecommendationsId = currentFeedRecommendationsId
    }
}