package com.recco.internal.feature.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recco.internal.core.logger.Logger
import com.recco.internal.core.model.feed.FeedSectionAndRecommendations
import com.recco.internal.core.model.feed.FeedSectionType.MENTAL_WELLBEING_EXPLORE
import com.recco.internal.core.model.feed.FeedSectionType.MENTAL_WELLBEING_RECOMMENDATIONS
import com.recco.internal.core.model.feed.FeedSectionType.MOST_POPULAR
import com.recco.internal.core.model.feed.FeedSectionType.NEW_CONTENT
import com.recco.internal.core.model.feed.FeedSectionType.NUTRITION_EXPLORE
import com.recco.internal.core.model.feed.FeedSectionType.NUTRITION_RECOMMENDATIONS
import com.recco.internal.core.model.feed.FeedSectionType.PHYSICAL_ACTIVITY_EXPLORE
import com.recco.internal.core.model.feed.FeedSectionType.PHYSICAL_ACTIVITY_RECOMMENDATIONS
import com.recco.internal.core.model.feed.FeedSectionType.PREFERRED_RECOMMENDATIONS
import com.recco.internal.core.model.feed.FeedSectionType.SLEEP_EXPLORE
import com.recco.internal.core.model.feed.FeedSectionType.SLEEP_RECOMMENDATIONS
import com.recco.internal.core.model.feed.FeedSectionType.STARTING_RECOMMENDATIONS
import com.recco.internal.core.repository.FeedRepository
import com.recco.internal.core.repository.MetricRepository
import com.recco.internal.core.repository.RecommendationRepository
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.core.ui.extensions.combine
import com.recco.internal.core.ui.pipelines.GlobalViewEvent.FeedSectionToUnlock
import com.recco.internal.core.ui.pipelines.globalViewEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
internal class FeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val recommendationRepository: RecommendationRepository,
    metricsRepository: MetricRepository,
    private val logger: Logger
) : ViewModel() {
    private val _viewState = MutableStateFlow(UiState<FeedUI>())
    val viewState: StateFlow<UiState<FeedUI>> = _viewState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = UiState()
    )

    init {
        setUpGlobalViewEvents()
        initialLoadOrRetry()
    }

    fun onUserInteract(userInteract: FeedUserInteract) {
        when (userInteract) {
            FeedUserInteract.Retry -> initialLoadOrRetry()
            FeedUserInteract.Refresh -> refresh()
            FeedUserInteract.RefreshUnlockedFeedSection -> updateFeedSectionState()
        }
    }

    private fun updateFeedSectionState() {
        viewModelScope.launch {
            _viewState.value.data?.feedSectionToUnlock?.let { feedSectionToUnlock ->
                _viewState.value = _viewState.value.copy(
                    data = _viewState.value.data?.copy(feedSectionToUnlock = null)
                )
                feedRepository.setFeedSectionState(
                    feedSectionToUnlock.type,
                    feedSectionToUnlock.state
                )
            }
        }
    }

    private fun setUpGlobalViewEvents() {
        viewModelScope.launch {
            globalViewEvents
                .filterIsInstance<FeedSectionToUnlock>()
                .collectLatest { feedSectionToUnlock ->
                    val data = _viewState.value.data ?: return@collectLatest
                    _viewState.value = _viewState.value.copy(
                        data = data.copy(feedSectionToUnlock = feedSectionToUnlock)
                    )

                    recommendationRepository.reloadSection(topic = feedSectionToUnlock.topic)
                }
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
                        recommendations = when (feedSection.type) {
                            PHYSICAL_ACTIVITY_RECOMMENDATIONS -> tailoredPhysicalActivity
                            NUTRITION_RECOMMENDATIONS -> tailoredNutrition
                            MENTAL_WELLBEING_RECOMMENDATIONS -> tailoredPhysicalWellbeing
                            SLEEP_RECOMMENDATIONS -> tailoredSleep
                            PREFERRED_RECOMMENDATIONS -> preferredRecommendations
                            MOST_POPULAR -> mostPopular
                            NEW_CONTENT -> newestContent
                            PHYSICAL_ACTIVITY_EXPLORE -> explorePhysicalActivity
                            NUTRITION_EXPLORE -> exploreNutrition
                            MENTAL_WELLBEING_EXPLORE -> explorePhysicalWellbeing
                            SLEEP_EXPLORE -> exploreSleep
                            STARTING_RECOMMENDATIONS -> starting
                        }
                    )
                }
            }.onStart {
                _viewState.value = _viewState.value.copy(error = null, isLoading = true)
            }.catch { error ->
                _viewState.value = _viewState.value.copy(error = error, isLoading = false)
                logger.e(error)
            }.collectLatest { sections ->
                val data = (_viewState.value.data ?: FeedUI())

                _viewState.value = _viewState.value.copy(
                    isLoading = false,
                    error = null,
                    data = data.copy(sections = sections)
                )
            }
        }
    }
}
