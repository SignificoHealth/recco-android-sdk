package com.shadowflight.core.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadowflight.core.logger.Logger
import com.shadowflight.core.model.feed.FeedSectionAndRecommendations
import com.shadowflight.core.model.feed.FeedSectionType.MOST_POPULAR
import com.shadowflight.core.model.feed.FeedSectionType.NEW_CONTENT
import com.shadowflight.core.model.feed.FeedSectionType.NUTRITION_EXPLORE
import com.shadowflight.core.model.feed.FeedSectionType.NUTRITION_RECOMMENDATIONS
import com.shadowflight.core.model.feed.FeedSectionType.PHYSICAL_ACTIVITY_EXPLORE
import com.shadowflight.core.model.feed.FeedSectionType.PHYSICAL_ACTIVITY_RECOMMENDATIONS
import com.shadowflight.core.model.feed.FeedSectionType.PHYSICAL_WELLBEING_EXPLORE
import com.shadowflight.core.model.feed.FeedSectionType.PHYSICAL_WELLBEING_RECOMMENDATIONS
import com.shadowflight.core.model.feed.FeedSectionType.PREFERRED_RECOMMENDATIONS
import com.shadowflight.core.model.feed.FeedSectionType.SLEEP_EXPLORE
import com.shadowflight.core.model.feed.FeedSectionType.SLEEP_RECOMMENDATIONS
import com.shadowflight.core.model.feed.FeedSectionType.STARTING_RECOMMENDATIONS
import com.shadowflight.core.repository.FeedRepository
import com.shadowflight.core.repository.RecommendationRepository
import com.shadowflight.core.ui.extensions.combine
import com.shadowflight.core.ui.models.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val recommendationRepository: RecommendationRepository,
    private val logger: Logger
) : ViewModel() {
    private val _viewState = MutableStateFlow(UiState<List<FeedSectionAndRecommendations>>())
    val viewState: Flow<UiState<List<FeedSectionAndRecommendations>>> = _viewState

    init {
        initialLoadOrRetry()
    }

    fun onUserInteract(userInteract: FeedUserInteract) {
        when (userInteract) {
            FeedUserInteract.Retry -> initialLoadOrRetry()
            FeedUserInteract.Refresh -> refresh()
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(error = null, isLoading = true)

            feedRepository.reloadFeed()
            recommendationRepository.reloadAllSections()
        }
    }

    private fun initialLoadOrRetry() {
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
                feedSections.map { feedSection ->
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
                }.filter {
                    it.recommendations.isNotEmpty() || it.feedSection.locked
                }
            }.onStart {
                _viewState.value = _viewState.value.copy(error = null, isLoading = true)
            }.catch { error ->
                _viewState.value = _viewState.value.copy(error = error, isLoading = false)
                logger.e(error)
            }.collectLatest { feedSectionAndRecommendations ->
                _viewState.value = _viewState.value.copy(
                    isLoading = false,
                    error = null,
                    data = feedSectionAndRecommendations
                )
            }
        }
    }
}