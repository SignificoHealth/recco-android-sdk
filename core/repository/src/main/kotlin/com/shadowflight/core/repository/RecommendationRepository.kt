package com.shadowflight.core.repository

import com.shadowflight.core.model.feed.FeedSection
import com.shadowflight.core.model.feed.FeedSectionType
import com.shadowflight.core.model.feed.Topic
import com.shadowflight.core.model.recommendation.Article
import com.shadowflight.core.model.recommendation.ContentId
import com.shadowflight.core.model.recommendation.Rating
import com.shadowflight.core.model.recommendation.Recommendation
import com.shadowflight.core.model.recommendation.Status
import com.shadowflight.core.network.http.unwrap
import com.shadowflight.core.openapi.api.RecommendationApi
import com.shadowflight.core.openapi.model.AppUserRecommendationDTO
import com.shadowflight.core.openapi.model.TopicDTO
import com.shadowflight.core.openapi.model.UpdateBookmarkDTO
import com.shadowflight.core.openapi.model.UpdateRatingDTO
import com.shadowflight.core.openapi.model.UpdateStatusDTO
import com.shadowflight.core.repository.mapper.asDTO
import com.shadowflight.core.repository.mapper.asEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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
        FeedSectionType.PHYSICAL_WELLBEING_RECOMMENDATIONS to Pipeline {
            api.getTailoredRecommendationsByTopic(TopicDTO.PHYSICAL_WELLBEING).unwrap()
                .map(AppUserRecommendationDTO::asEntity)
        },
        FeedSectionType.PHYSICAL_WELLBEING_EXPLORE to Pipeline {
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

    val explorePhysicalActivity =
        sectionsPipelines[FeedSectionType.PHYSICAL_ACTIVITY_EXPLORE]!!.state

    val tailoredNutrition = sectionsPipelines[FeedSectionType.NUTRITION_RECOMMENDATIONS]!!.state

    val exploreNutrition = sectionsPipelines[FeedSectionType.NUTRITION_EXPLORE]!!.state

    val tailoredPhysicalWellbeing =
        sectionsPipelines[FeedSectionType.PHYSICAL_WELLBEING_RECOMMENDATIONS]!!.state

    val explorePhysicalWellbeing =
        sectionsPipelines[FeedSectionType.PHYSICAL_WELLBEING_EXPLORE]!!.state

    val tailoredSleep = sectionsPipelines[FeedSectionType.SLEEP_RECOMMENDATIONS]!!.state

    val exploreSleep = sectionsPipelines[FeedSectionType.SLEEP_EXPLORE]!!.state

    val preferredRecommendations =
        sectionsPipelines[FeedSectionType.PREFERRED_RECOMMENDATIONS]!!.state

    val mostPopular = sectionsPipelines[FeedSectionType.MOST_POPULAR]!!.state

    val newestContent = sectionsPipelines[FeedSectionType.NEW_CONTENT]!!.state

    val starting = sectionsPipelines[FeedSectionType.STARTING_RECOMMENDATIONS]!!.state

    suspend fun reloadAllSections() {
        sectionsPipelines.forEach { (_, pipeline) -> pipeline.update() }
    }

    suspend fun getArticle(contentId: ContentId): Article =
        api.getArticle(itemId = contentId.itemId, catalogId = contentId.catalogId)
            .unwrap().asEntity()

    suspend fun setBookmarkRecommendation(contentId: ContentId, bookmarked: Boolean) {
        api.setBookmark(
            UpdateBookmarkDTO(
                contentId = contentId.asDTO(),
                bookmarked = bookmarked,
                contentType = UpdateBookmarkDTO.ContentType.ARTICLES
            )
        )
        reloadSectionBasedOnContentId(contentId)
    }

    suspend fun setRecommendationRating(contentId: ContentId, rating: Rating) {
        api.setRating(
            UpdateRatingDTO(
                contentId = contentId.asDTO(),
                contentType = UpdateRatingDTO.ContentType.ARTICLES,
                rating = when (rating) {
                    Rating.LIKE -> UpdateRatingDTO.Rating.LIKE
                    Rating.DISLIKE -> UpdateRatingDTO.Rating.DISLIKE
                    Rating.NOT_RATED -> UpdateRatingDTO.Rating.NOT_RATED
                }
            )
        )
        reloadSectionBasedOnContentId(contentId)
    }

    suspend fun setRecommendationAsViewed(contentId: ContentId) {
        api.setStatus(
            UpdateStatusDTO(
                contentId = contentId.asDTO(),
                contentType = UpdateStatusDTO.ContentType.ARTICLES,
                status = UpdateStatusDTO.Status.VIEWED
            )
        )
        reloadSectionBasedOnContentId(contentId)
    }

    private suspend fun reloadSectionBasedOnContentId(contentId: ContentId) {
        sectionsPipelines
            .filter { (_, pipeline) -> pipeline.value?.any { it.id == contentId } == true }
            .forEach { (_, pipeline) -> pipeline.update()  }
    }
}