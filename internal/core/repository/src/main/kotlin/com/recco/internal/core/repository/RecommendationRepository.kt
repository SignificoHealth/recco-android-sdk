package com.recco.internal.core.repository

import com.recco.internal.core.model.FlowDataState
import com.recco.internal.core.model.feed.FeedSectionType
import com.recco.internal.core.model.feed.Topic
import com.recco.internal.core.model.recommendation.Article
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.Status
import com.recco.internal.core.network.http.unwrap
import com.recco.internal.core.openapi.api.RecommendationApi
import com.recco.internal.core.openapi.model.AppUserRecommendationDTO
import com.recco.internal.core.openapi.model.ContentTypeDTO
import com.recco.internal.core.openapi.model.TopicDTO
import com.recco.internal.core.openapi.model.UpdateBookmarkDTO
import com.recco.internal.core.openapi.model.UpdateRatingDTO
import com.recco.internal.core.repository.mapper.asDTO
import com.recco.internal.core.repository.mapper.asEntity
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class RecommendationRepository @Inject constructor(
    private val api: RecommendationApi
) {
    private val sectionsPipelines = mapOf(
        FeedSectionType.PHYSICAL_ACTIVITY_RECOMMENDATIONS to PipelineStateAware {
            api.getTailoredRecommendationsByTopic(TopicDTO.PHYSICAL_ACTIVITY).unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.PHYSICAL_ACTIVITY_EXPLORE to PipelineStateAware {
            api.exploreContentByTopic(TopicDTO.PHYSICAL_ACTIVITY).unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.NUTRITION_RECOMMENDATIONS to PipelineStateAware {
            api.getTailoredRecommendationsByTopic(TopicDTO.NUTRITION).unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.NUTRITION_EXPLORE to PipelineStateAware {
            api.exploreContentByTopic(TopicDTO.NUTRITION).unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.MENTAL_WELLBEING_RECOMMENDATIONS to PipelineStateAware {
            api.getTailoredRecommendationsByTopic(TopicDTO.PHYSICAL_WELLBEING).unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.MENTAL_WELLBEING_EXPLORE to PipelineStateAware {
            api.exploreContentByTopic(TopicDTO.PHYSICAL_WELLBEING).unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.SLEEP_RECOMMENDATIONS to PipelineStateAware {
            api.getTailoredRecommendationsByTopic(TopicDTO.SLEEP).unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.SLEEP_EXPLORE to PipelineStateAware {
            api.exploreContentByTopic(TopicDTO.SLEEP).unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.PREFERRED_RECOMMENDATIONS to PipelineStateAware {
            api.getUserPreferredRecommendations().unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.MOST_POPULAR to PipelineStateAware {
            api.getMostPopularContent().unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.NEW_CONTENT to PipelineStateAware {
            api.getNewestContent().unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.STARTING_RECOMMENDATIONS to PipelineStateAware {
            api.getStartingRecommendations().unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        }
    )

    val tailoredPhysicalActivity =
        sectionsPipelines[FeedSectionType.PHYSICAL_ACTIVITY_RECOMMENDATIONS]!!.state

    val explorePhysicalActivity =
        sectionsPipelines[FeedSectionType.PHYSICAL_ACTIVITY_EXPLORE]!!.state

    val tailoredNutrition = sectionsPipelines[FeedSectionType.NUTRITION_RECOMMENDATIONS]!!.state

    val exploreNutrition = sectionsPipelines[FeedSectionType.NUTRITION_EXPLORE]!!.state

    val tailoredPhysicalWellbeing =
        sectionsPipelines[FeedSectionType.MENTAL_WELLBEING_RECOMMENDATIONS]!!.state

    val explorePhysicalWellbeing =
        sectionsPipelines[FeedSectionType.MENTAL_WELLBEING_EXPLORE]!!.state

    val tailoredSleep = sectionsPipelines[FeedSectionType.SLEEP_RECOMMENDATIONS]!!.state

    val exploreSleep = sectionsPipelines[FeedSectionType.SLEEP_EXPLORE]!!.state

    val preferredRecommendations =
        sectionsPipelines[FeedSectionType.PREFERRED_RECOMMENDATIONS]!!.state

    val mostPopular = sectionsPipelines[FeedSectionType.MOST_POPULAR]!!.state

    val newestContent = sectionsPipelines[FeedSectionType.NEW_CONTENT]!!.state

    val starting = sectionsPipelines[FeedSectionType.STARTING_RECOMMENDATIONS]!!.state

    suspend fun reloadAllSections() {
        sectionsPipelines.forEach { (_, pipeline) -> pipeline.reloadRemoteDatasource() }
    }

    suspend fun reloadSection(topic: Topic) {
        val sectionType = when (topic) {
            Topic.PHYSICAL_ACTIVITY -> FeedSectionType.PHYSICAL_ACTIVITY_RECOMMENDATIONS
            Topic.NUTRITION -> FeedSectionType.NUTRITION_RECOMMENDATIONS
            Topic.MENTAL_WELLBEING -> FeedSectionType.MENTAL_WELLBEING_RECOMMENDATIONS
            Topic.SLEEP -> FeedSectionType.SLEEP_RECOMMENDATIONS
        }
        sectionsPipelines[sectionType]?.reloadRemoteDatasource()
    }

    private val bookmarksPipeline = Pipeline {
        api.getBookmarkedRecommendations().unwrap()
            .map(AppUserRecommendationDTO::asEntity)
    }

    val bookmarks = bookmarksPipeline.state

    suspend fun reloadBookmarks() {
        bookmarksPipeline.reloadRemoteDatasource()
    }

    suspend fun getArticle(contentId: ContentId): Article =
        api.getArticle(itemId = contentId.itemId, catalogId = contentId.catalogId)
            .unwrap().asEntity()

    suspend fun setBookmarkRecommendation(contentId: ContentId, bookmarked: Boolean) {
        api.setBookmark(
            UpdateBookmarkDTO(
                contentId = contentId.asDTO(),
                bookmarked = bookmarked,
                contentType = ContentTypeDTO.ARTICLES
            )
        )
        updateSections(contentId = contentId, bookmarked = bookmarked)
        reloadBookmarks()
    }

    suspend fun setRecommendationRating(contentId: ContentId, rating: Rating) {
        api.setRating(
            UpdateRatingDTO(
                contentId = contentId.asDTO(),
                contentType = ContentTypeDTO.ARTICLES,
                rating = rating.asDTO()
            )
        )
        updateSections(contentId = contentId, rating = rating)
    }

    suspend fun setRecommendationAsViewed(contentId: ContentId) {
        updateSections(contentId = contentId, status = Status.VIEWED)
    }

    private suspend fun updateSections(
        contentId: ContentId,
        status: Status? = null,
        bookmarked: Boolean? = null,
        rating: Rating? = null
    ) {
        sectionsPipelines
            .filter { (_, pipeline) -> pipeline.value is FlowDataState.Success }
            .filter { (_, pipeline) ->
                val data = (pipeline.value as FlowDataState.Success).data
                data.any { it.id == contentId }
            }
            .forEach { (_, pipeline) ->
                val data = (pipeline.value as FlowDataState.Success).data
                pipeline.replaceWithLocal(
                    data.map { recommendation ->
                        if (recommendation.id == contentId) {
                            recommendation.copy(
                                status = status ?: recommendation.status,
                                bookmarked = bookmarked ?: recommendation.bookmarked,
                                rating = rating ?: recommendation.rating,
                            )
                        } else {
                            recommendation
                        }
                    }
                )
            }
    }
}