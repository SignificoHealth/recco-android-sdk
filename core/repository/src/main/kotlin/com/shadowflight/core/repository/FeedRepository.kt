package com.shadowflight.core.repository

import com.shadowflight.core.model.feed.FeedSection
import com.shadowflight.core.model.feed.FeedSectionType
import com.shadowflight.core.model.feed.LockType
import com.shadowflight.core.network.http.unwrap
import com.shadowflight.core.openapi.api.FeedApi
import com.shadowflight.core.openapi.model.FeedSectionDTO
import com.shadowflight.core.repository.mapper.asEntity
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

    suspend fun setFeedSectionAsUnlocked(feedSectionType: FeedSectionType) {
        feedSectionsPipeline.value?.map { feedSection ->
            if (feedSection.type == feedSectionType) {
                feedSection.copy(
                    type = feedSection.type,
                    locked = LockType.UNLOCKED,
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
