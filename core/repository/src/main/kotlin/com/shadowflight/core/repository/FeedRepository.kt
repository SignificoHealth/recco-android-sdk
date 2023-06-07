package com.shadowflight.core.repository

import com.shadowflight.core.model.feed.FeedSection
import com.shadowflight.core.network.http.unwrap
import com.shadowflight.core.openapi.api.FeedApi
import com.shadowflight.core.openapi.model.FeedSectionDTO
import com.shadowflight.core.repository.mapper.asEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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
}