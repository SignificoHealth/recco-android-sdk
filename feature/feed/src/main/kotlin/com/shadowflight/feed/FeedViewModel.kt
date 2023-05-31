package com.shadowflight.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadowflight.logger.Logger
import com.shadowflight.model.feed.FeedSectionType.*
import com.shadowflight.model.feed.*
import com.shadowflight.repository.FeedRepository
import com.shadowflight.repository.RecommendationRepository
import com.shadowflight.uicommons.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val recommendationRepository: RecommendationRepository,
    private val logger: Logger
) : ViewModel() {
    private val _viewState = MutableStateFlow(FeedViewUIState(isLoading = true))
    val viewState: Flow<FeedViewUIState> = _viewState

    init {
        initialLoadSubscribe()
    }

    fun onUserInteract(userInteract: FeedUserInteract) {
        when (userInteract) {
            FeedUserInteract.Retry -> initialLoadSubscribe()
        }
    }

    private fun initialLoadSubscribe() {
        viewModelScope.launch {
            combine(
                feedRepository.feedSections,
                recommendationRepository.tailoredPhysicalActivity,
                recommendationRepository.explorePhysicalActivity,
                recommendationRepository.tailoredNutrition,
                recommendationRepository.exploreNutrition,
                recommendationRepository.tailoredPhysicalWellbeing,
                recommendationRepository.explorePhysicalWellbeing,
                recommendationRepository.tailoredSleep,
                recommendationRepository.exploreSleep,
                recommendationRepository.preferredRecommendations,
                recommendationRepository.mostPopular,
                recommendationRepository.newestContent,
                recommendationRepository.starting
            ) { feedSections,
                tailoredPhysicalActivity, explorePhysicalActivity,
                tailoredNutrition, exploreNutrition,
                tailoredPhysicalWellbeing, explorePhysicalWellbeing,
                tailoredSleep, exploreSleep,
                preferredRecommendations,
                mostPopular,
                newestContent,
                starting ->
                FeedViewUIState(
                    isLoading = false,
                    isError = false,
                    feedSectionAndRecommendations = feedSections.map { feedSection ->
                        FeedSectionAndRecommendations(
                            feedSection = feedSection,
                            recommendations = if (feedSection.locked) {
                                emptyList()
                            } else {
                                when (feedSection.type) {
                                    PHYSICAL_ACTIVITY_RECOMMENDATIONS -> tailoredPhysicalActivity
                                    NUTRITION_RECOMMENDATIONS -> tailoredNutrition
                                    PHYSICAL_WELLBEING_RECOMMENDATIONS -> tailoredPhysicalWellbeing
                                    SLEEP_RECOMMENDATIONS -> tailoredSleep
                                    PREFERRED_RECOMMENDATIONS -> preferredRecommendations
                                    MOST_POPULAR -> mostPopular
                                    NEW_CONTENT -> newestContent
                                    PHYSICAL_ACTIVITY_EXPLORE -> explorePhysicalActivity
                                    NUTRITION_EXPLORE -> exploreNutrition
                                    PHYSICAL_WELLBEING_EXPLORE -> explorePhysicalWellbeing
                                    SLEEP_EXPLORE -> exploreSleep
                                    STARTING_RECOMMENDATIONS -> starting
                                }
                            }
                        )
                    }
                )
            }.catch {
                emit(FeedViewUIState(isError = true, isLoading = false))
                logger.e(it)
            }.collectLatest { uiState ->
                _viewState.emit(uiState)
            }
        }
    }
}

data class FeedViewUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val feedSectionAndRecommendations: List<FeedSectionAndRecommendations> = emptyList()
)