package com.shadowflight.core.repository

import com.shadowflight.core.model.feed.FeedSectionType
import com.shadowflight.core.model.feed.Topic
import com.shadowflight.core.model.recommendation.Article
import com.shadowflight.core.model.recommendation.ContentId
import com.shadowflight.core.model.recommendation.Rating
import com.shadowflight.core.model.recommendation.Status
import com.shadowflight.core.network.http.unwrap
import com.shadowflight.core.openapi.api.RecommendationApi
import com.shadowflight.core.openapi.model.AppUserRecommendationDTO
import com.shadowflight.core.openapi.model.ContentTypeDTO
import com.shadowflight.core.openapi.model.StatusDTO
import com.shadowflight.core.openapi.model.TopicDTO
import com.shadowflight.core.openapi.model.UpdateBookmarkDTO
import com.shadowflight.core.openapi.model.UpdateRatingDTO
import com.shadowflight.core.openapi.model.UpdateStatusDTO
import com.shadowflight.core.repository.mapper.asDTO
import com.shadowflight.core.repository.mapper.asEntity
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class RecommendationRepository @Inject constructor(
    private val api: RecommendationApi
) {
    private val sectionsPipelines = mapOf(
        FeedSectionType.PHYSICAL_ACTIVITY_RECOMMENDATIONS to Pipeline {
            api.getTailoredRecommendationsByTopic(TopicDTO.PHYSICAL_ACTIVITY).unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.PHYSICAL_ACTIVITY_EXPLORE to Pipeline {
            api.exploreContentByTopic(TopicDTO.PHYSICAL_ACTIVITY).unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.NUTRITION_RECOMMENDATIONS to Pipeline {
            api.getTailoredRecommendationsByTopic(TopicDTO.NUTRITION).unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.NUTRITION_EXPLORE to Pipeline {
            api.exploreContentByTopic(TopicDTO.NUTRITION).unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.MENTAL_WELLBEING_RECOMMENDATIONS to Pipeline {
            api.getTailoredRecommendationsByTopic(TopicDTO.PHYSICAL_WELLBEING).unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.MENTAL_WELLBEING_EXPLORE to Pipeline {
            api.exploreContentByTopic(TopicDTO.PHYSICAL_WELLBEING).unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.SLEEP_RECOMMENDATIONS to Pipeline {
            api.getTailoredRecommendationsByTopic(TopicDTO.SLEEP).unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.SLEEP_EXPLORE to Pipeline {
            api.exploreContentByTopic(TopicDTO.SLEEP).unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.PREFERRED_RECOMMENDATIONS to Pipeline {
            api.getUserPreferredRecommendations().unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.MOST_POPULAR to Pipeline {
            api.getMostPopularContent().unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.NEW_CONTENT to Pipeline {
            api.getNewestContent().unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.STARTING_RECOMMENDATIONS to Pipeline {
            api.getStartingRecommendations().unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        }
    )

    val tailoredPhysicalActivity =
        sectionsPipelines[FeedSectionType.PHYSICAL_ACTIVITY_RECOMMENDATIONS]!!.state
    val tailoredPhysicalActivityPipelineId
        get() = sectionsPipelines[FeedSectionType.PHYSICAL_ACTIVITY_RECOMMENDATIONS]!!.id

    val explorePhysicalActivity =
        sectionsPipelines[FeedSectionType.PHYSICAL_ACTIVITY_EXPLORE]!!.state
    val explorePhysicalActivityPipelineId
        get() = sectionsPipelines[FeedSectionType.PHYSICAL_ACTIVITY_EXPLORE]!!.id

    val tailoredNutrition = sectionsPipelines[FeedSectionType.NUTRITION_RECOMMENDATIONS]!!.state
    val tailoredNutritionPipelineId
        get() = sectionsPipelines[FeedSectionType.NUTRITION_RECOMMENDATIONS]!!.id

    val exploreNutrition = sectionsPipelines[FeedSectionType.NUTRITION_EXPLORE]!!.state
    val exploreNutritionPipelineId
        get() = sectionsPipelines[FeedSectionType.NUTRITION_EXPLORE]!!.id

    val tailoredPhysicalWellbeing =
        sectionsPipelines[FeedSectionType.MENTAL_WELLBEING_RECOMMENDATIONS]!!.state
    val tailoredPhysicalWellbeingPipelineId
        get() = sectionsPipelines[FeedSectionType.MENTAL_WELLBEING_RECOMMENDATIONS]!!.id

    val explorePhysicalWellbeing =
        sectionsPipelines[FeedSectionType.MENTAL_WELLBEING_EXPLORE]!!.state
    val explorePhysicalWellbeingPipelineId
        get() = sectionsPipelines[FeedSectionType.MENTAL_WELLBEING_EXPLORE]!!.id

    val tailoredSleep = sectionsPipelines[FeedSectionType.SLEEP_RECOMMENDATIONS]!!.state
    val tailoredSleepPipelineId
        get() = sectionsPipelines[FeedSectionType.SLEEP_RECOMMENDATIONS]!!.id

    val exploreSleep = sectionsPipelines[FeedSectionType.SLEEP_EXPLORE]!!.state
    val exploreSleepPipelineId
        get() = sectionsPipelines[FeedSectionType.SLEEP_EXPLORE]!!.id

    val preferredRecommendations =
        sectionsPipelines[FeedSectionType.PREFERRED_RECOMMENDATIONS]!!.state
    val preferredRecommendationsPipelineId
        get() = sectionsPipelines[FeedSectionType.PREFERRED_RECOMMENDATIONS]!!.id

    val mostPopular = sectionsPipelines[FeedSectionType.MOST_POPULAR]!!.state
    val mostPopularPipelineId
        get() = sectionsPipelines[FeedSectionType.MOST_POPULAR]!!.id

    val newestContent = sectionsPipelines[FeedSectionType.NEW_CONTENT]!!.state
    val newestContentPipelineId
        get() = sectionsPipelines[FeedSectionType.NEW_CONTENT]!!.id

    val starting = sectionsPipelines[FeedSectionType.STARTING_RECOMMENDATIONS]!!.state
    val startingPipelineId
        get() = sectionsPipelines[FeedSectionType.STARTING_RECOMMENDATIONS]!!.id

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
        api.setStatus(
            UpdateStatusDTO(
                contentId = contentId.asDTO(),
                contentType = ContentTypeDTO.ARTICLES,
                status = StatusDTO.VIEWED
            )
        )
        updateSections(contentId = contentId, status = Status.VIEWED)
    }

    private suspend fun updateSections(
        contentId: ContentId,
        status: Status? = null,
        bookmarked: Boolean? = null,
        rating: Rating? = null
    ) {
        sectionsPipelines
            .filter { (_, pipeline) -> pipeline.value != null }
            .filter { (_, pipeline) -> pipeline.value!!.any { it.id == contentId } }
            .forEach { (_, pipeline) ->
                pipeline.replaceWithLocal(
                    pipeline.value!!.map { recommendation ->
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