package com.shadowflight.core.repository

import com.shadowflight.core.model.recommendation.Article
import com.shadowflight.core.model.recommendation.ContentId
import com.shadowflight.core.network.http.unwrap
import com.shadowflight.core.openapi.api.RecommendationApi
import com.shadowflight.core.openapi.model.AppUserRecommendationDTO
import com.shadowflight.core.openapi.model.TopicDTO
import com.shadowflight.core.repository.mapper.asEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecommendationRepository @Inject constructor(
    private val api: RecommendationApi
) {
    // Physical activity
    private val tailoredPhysicalActivityPipeline = Pipeline {
        api.getTailoredRecommendationsByTopic(TopicDTO.PHYSICAL_ACTIVITY).unwrap()
            .map(AppUserRecommendationDTO::asEntity)
    }
    val tailoredPhysicalActivity = tailoredPhysicalActivityPipeline.state

    suspend fun reloadTailoredPhysicalActivity() {
        tailoredPhysicalActivityPipeline.update()
    }

    private val explorePhysicalActivityPipeline = Pipeline {
        api.exploreContentByTopic(TopicDTO.PHYSICAL_ACTIVITY).unwrap()
            .map(AppUserRecommendationDTO::asEntity)
    }
    val explorePhysicalActivity = explorePhysicalActivityPipeline.state

    suspend fun reloadExplorePhysicalActivity() {
        explorePhysicalActivityPipeline.update()
    }

    // Nutrition
    private val tailoredNutritionPipeline = Pipeline {
        api.getTailoredRecommendationsByTopic(TopicDTO.NUTRITION).unwrap()
            .map(AppUserRecommendationDTO::asEntity)
    }
    val tailoredNutrition = tailoredNutritionPipeline.state

    suspend fun reloadTailoredNutrition() {
        tailoredNutritionPipeline.update()
    }

    private val exploreNutritionPipeline = Pipeline {
        api.exploreContentByTopic(TopicDTO.NUTRITION).unwrap()
            .map(AppUserRecommendationDTO::asEntity)
    }
    val exploreNutrition = exploreNutritionPipeline.state

    suspend fun reloadNutrition() {
        exploreNutritionPipeline.update()
    }

    // Physical wellbeing
    private val tailoredPhysicalWellbeingPipeline = Pipeline {
        api.getTailoredRecommendationsByTopic(TopicDTO.PHYSICAL_WELLBEING).unwrap()
            .map(AppUserRecommendationDTO::asEntity)
    }
    val tailoredPhysicalWellbeing = tailoredPhysicalWellbeingPipeline.state

    suspend fun reloadTailoredPhysicalWellbeing() {
        tailoredPhysicalWellbeingPipeline.update()
    }

    private val explorePhysicalWellbeingPipeline = Pipeline {
        api.exploreContentByTopic(TopicDTO.PHYSICAL_WELLBEING).unwrap()
            .map(AppUserRecommendationDTO::asEntity)
    }
    val explorePhysicalWellbeing = explorePhysicalWellbeingPipeline.state

    suspend fun reloadExplorePhysicalWellbeing() {
        explorePhysicalWellbeingPipeline.update()
    }

    // Sleep
    private val tailoredSleepPipeline = Pipeline {
        api.getTailoredRecommendationsByTopic(TopicDTO.SLEEP).unwrap()
            .map(AppUserRecommendationDTO::asEntity)
    }
    val tailoredSleep = tailoredSleepPipeline.state

    suspend fun reloadTailoredSleep() {
        tailoredSleepPipeline.update()
    }

    private val exploreSleepPipeline = Pipeline {
        api.exploreContentByTopic(TopicDTO.SLEEP).unwrap()
            .map(AppUserRecommendationDTO::asEntity)
    }
    val exploreSleep = exploreSleepPipeline.state

    suspend fun reloadExploreSleep() {
        exploreSleepPipeline.update()
    }

    // Preferred recommendations
    private val preferredRecommendationsPipeline = Pipeline {
        api.getUserPreferredRecommendations().unwrap()
            .map(AppUserRecommendationDTO::asEntity)
    }
    val preferredRecommendations = preferredRecommendationsPipeline.state

    // Most popular
    private val mostPopularPipeline = Pipeline {
        api.getMostPopularContent().unwrap()
            .map(AppUserRecommendationDTO::asEntity)
    }
    val mostPopular = mostPopularPipeline.state

    // Newest content
    private val newestContentPipeline = Pipeline {
        api.getNewestContent().unwrap()
            .map(AppUserRecommendationDTO::asEntity)
    }
    val newestContent = newestContentPipeline.state

    // Starting
    private val startingPipeline = Pipeline {
        api.getStartingRecommendations().unwrap()
            .map(AppUserRecommendationDTO::asEntity)
    }
    val starting = startingPipeline.state

    suspend fun getArticle(contentId: ContentId): Article =
        api.getArticle(itemId = contentId.itemId, catalogId = contentId.catalogId)
            .unwrap().asEntity()
}