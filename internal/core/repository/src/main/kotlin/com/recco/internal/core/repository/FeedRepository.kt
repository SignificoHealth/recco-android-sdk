package com.recco.internal.core.repository

import com.recco.internal.core.model.feed.FeedSection
import com.recco.internal.core.model.feed.FeedSectionState
import com.recco.internal.core.model.feed.FeedSectionType
import com.recco.internal.core.network.http.unwrap
import com.recco.internal.core.openapi.api.FeedApi
import com.recco.internal.core.openapi.model.FeedSectionDTO
import com.recco.internal.core.repository.mapper.asEntity
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class FeedRepository @Inject constructor(
    private val api: FeedApi
) {
    private val feedSectionsPipeline = Pipeline(this::getFeedSections)
    val feedSections = feedSectionsPipeline.state

    suspend fun reloadFeed() {
        feedSectionsPipeline.reloadRemoteDatasource()
    }

    private suspend fun getFeedSections(): List<FeedSection> =
        api.getFeed().unwrap().map(FeedSectionDTO::asEntity)

    suspend fun setFeedSectionState(
        feedSectionType: FeedSectionType,
        feedSectionState: FeedSectionState
    ) {
        feedSectionsPipeline.value?.map { feedSection ->
            if (feedSection.type == feedSectionType) {
                feedSection.copy(
                    type = feedSection.type,
                    state = feedSectionState,
                    topic = feedSection.topic
                )
            } else {
                feedSection
            }
        }?.let { sectionsUpdated ->
            feedSectionsPipeline.replaceWithLocal(sectionsUpdated)
        }
    }
}
