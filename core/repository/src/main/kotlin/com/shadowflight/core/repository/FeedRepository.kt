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
    val feedSectionsPipelineId: Int
        get() = feedSectionsPipeline.id

    suspend fun reloadFeed() {
        feedSectionsPipeline.reloadRemoteDatasource()
    }

    private suspend fun getFeedSections(): List<FeedSection> =
        api.getFeed().unwrap().map(FeedSectionDTO::asEntity)

    suspend fun setFeedSectionAsUnlocked(feedSectionType: FeedSectionType?): Boolean {
        return if (feedSectionsPipeline.value == null || feedSectionType == null) {
            false
        } else {
            val sectionsUpdated = feedSectionsPipeline.value!!.map { feedSection ->
                if (feedSection.type == feedSectionType && feedSection.locked == LockType.UNLOCKING) {
                    feedSection.copy(
                        type = feedSection.type,
                        locked = LockType.UNLOCKED,
                        topic = feedSection.topic
                    )
                } else {
                    feedSection
                }
            }
            feedSectionsPipeline.replaceWithLocal(sectionsUpdated)
            true
        }
    }

    suspend fun moveUnlockedFeedSectionAtTop(feedSectionType: FeedSectionType) {
        val sectionsUpdated = listOf(
            FeedSection(
                type = feedSectionType,
                locked = LockType.UNLOCKING,
                topic = null
            )
        ).plus(feedSectionsPipeline.value!!.filterNot { it.type == feedSectionType })

        feedSectionsPipeline.replaceWithLocal(sectionsUpdated)
    }
}
