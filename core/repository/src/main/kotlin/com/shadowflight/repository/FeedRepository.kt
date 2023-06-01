package com.shadowflight.repository

import com.shadowflight.model.feed.FeedSection
import com.shadowflight.network.http.unwrap
import com.shadowflight.openapi.api.FeedApi
import com.shadowflight.openapi.model.FeedSectionDTO
import com.shadowflight.repository.mapper.asEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepository @Inject constructor(
    private val api: FeedApi
) {
    private val feedSectionsPipeline = Pipeline(this::getFeedSections)
    val feedSections = feedSectionsPipeline.state

    suspend fun reloadFeed() {
        feedSectionsPipeline.update()
    }

    private suspend fun getFeedSections(): List<FeedSection> =
        api.getFeed().unwrap().map(FeedSectionDTO::asEntity)
}