package com.shadowflight.core.feed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadowflight.core.logger.Logger
import com.shadowflight.core.model.feed.FeedSectionAndRecommendations
import com.shadowflight.core.model.feed.FeedSectionType
import com.shadowflight.core.model.feed.FeedSectionType.MENTAL_WELLBEING_EXPLORE
import com.shadowflight.core.model.feed.FeedSectionType.MENTAL_WELLBEING_RECOMMENDATIONS
import com.shadowflight.core.model.feed.FeedSectionType.MOST_POPULAR
import com.shadowflight.core.model.feed.FeedSectionType.NEW_CONTENT
import com.shadowflight.core.model.feed.FeedSectionType.NUTRITION_EXPLORE
import com.shadowflight.core.model.feed.FeedSectionType.NUTRITION_RECOMMENDATIONS
import com.shadowflight.core.model.feed.FeedSectionType.PHYSICAL_ACTIVITY_EXPLORE
import com.shadowflight.core.model.feed.FeedSectionType.PHYSICAL_ACTIVITY_RECOMMENDATIONS
import com.shadowflight.core.model.feed.FeedSectionType.PREFERRED_RECOMMENDATIONS
import com.shadowflight.core.model.feed.FeedSectionType.SLEEP_EXPLORE
import com.shadowflight.core.model.feed.FeedSectionType.SLEEP_RECOMMENDATIONS
import com.shadowflight.core.model.feed.FeedSectionType.STARTING_RECOMMENDATIONS
import com.shadowflight.core.model.feed.LockType
import com.shadowflight.core.repository.FeedRepository
import com.shadowflight.core.repository.RecommendationRepository
import com.shadowflight.core.ui.TriggerState
import com.shadowflight.core.ui.components.UiState
import com.shadowflight.core.ui.extensions.combine
import com.shadowflight.core.ui.pipelines.GlobalViewEvent
import com.shadowflight.core.ui.pipelines.globalViewEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val recommendationRepository: RecommendationRepository,
    private val logger: Logger
) : ViewModel() {
    private val _viewState = MutableStateFlow(UiState<FeedUI>())
    val viewState: StateFlow<UiState<FeedUI>> = _viewState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = UiState()
    )
    private var isShowingUnlockedSection = false
    private var forceShowLoading = false
    private var previousFeedSectionId = 0
    private var previousFeedRecommendationsId = 0
    private var currentFeedSectionId = 0
    private var currentFeedRecommendationsId = 0

    init {
        setUpGlobalViewEvents()
        initialLoadOrRetry()
    }

    fun onUserInteract(userInteract: FeedUserInteract) {
        when (userInteract) {
            FeedUserInteract.Retry -> initialLoadOrRetry()
            FeedUserInteract.Refresh -> refresh()
            FeedUserInteract.RefreshUnlockedFeedSection -> {
                viewModelScope.launch {
                    val feedSectionType = _viewState.value.data?.feedSectionTypeToUnlock
                    if (!isShowingUnlockedSection && feedRepository.setFeedSectionAsUnlocked(
                            feedSectionType = feedSectionType
                        )
                    ) {
                        isShowingUnlockedSection = true
                        forceLoadingState(showLoading = false)
                        Log.e("XXX", "RefreshUnlockedFeedSection=$feedSectionType")
                    }
                }
            }
            FeedUserInteract.ResetUnlockedFeedSection -> {
                resetPipelineIds(_viewState.value.data?.feedSectionTypeToUnlock)
                Log.e("XXX", "ResetUnlockedFeedSection")
                _viewState.value = _viewState.value.copy(
                    data = _viewState.value.data?.copy(feedSectionTypeToUnlock = null),
                )
                isShowingUnlockedSection = false
                forceLoadingState(showLoading = false)
            }
        }
    }

    private fun setUpGlobalViewEvents() {
        viewModelScope.launch {
            globalViewEvents
                .filter { it is GlobalViewEvent.ResetFeedScroll }
                .collectLatest {
                    val (topic, feedSectionType) = (it as GlobalViewEvent.ResetFeedScroll)
                    resetPipelineIds(feedSectionType)

                    _viewState.value = _viewState.value.copy(
                        data = _viewState.value.data?.copy(
                            feedSectionTypeToUnlock = feedSectionType,
                            resetScrollTriggerState = TriggerState().apply { trigger() }
                        ),
                    )
                    Log.e("XXX", "ResetFeedScroll")
                    feedRepository.moveUnlockedFeedSectionAtTop(feedSectionType!!)
                    // time enough to perform the scroll animations
                    delay(500)
                    forceLoadingState(showLoading = true)
                    feedRepository.reloadFeed()
                    recommendationRepository.reloadSection(topic = topic!!)
                }
        }
    }

    private fun forceLoadingState(showLoading: Boolean) {
        forceShowLoading = showLoading
        _viewState.value = _viewState.value.copy(
            isLoading = forceShowLoading,
        )
    }

    private fun isContentUpdated(feedSectionType: FeedSectionType?): Boolean {
        currentFeedSectionId = feedRepository.feedSectionsPipelineId
        currentFeedRecommendationsId = getRecommendationsPipelineId(feedSectionType)

//        Log.e(
//            "XXX",
//            "($currentFeedSectionId,$currentFeedRecommendationsId) == ($previousFeedSectionId,$previousFeedRecommendationsId)"
//        )

        return previousFeedSectionId != currentFeedSectionId
                && previousFeedRecommendationsId != currentFeedRecommendationsId
                && previousFeedRecommendationsId != 0
                && currentFeedRecommendationsId != 0
    }

    private fun resetPipelineIds(feedSectionType: FeedSectionType?) {
        previousFeedSectionId = feedRepository.feedSectionsPipelineId
        previousFeedRecommendationsId = getRecommendationsPipelineId(feedSectionType)
        currentFeedSectionId = previousFeedSectionId
        currentFeedRecommendationsId = previousFeedRecommendationsId
    }

    private fun getRecommendationsPipelineId(feedSectionType: FeedSectionType?) =
        when (feedSectionType) {
            PHYSICAL_ACTIVITY_RECOMMENDATIONS -> recommendationRepository.tailoredPhysicalActivityPipelineId
            NUTRITION_RECOMMENDATIONS -> recommendationRepository.tailoredNutritionPipelineId
            MENTAL_WELLBEING_RECOMMENDATIONS -> recommendationRepository.tailoredPhysicalWellbeingPipelineId
            SLEEP_RECOMMENDATIONS -> recommendationRepository.tailoredSleepPipelineId
            PHYSICAL_ACTIVITY_EXPLORE -> recommendationRepository.explorePhysicalActivityPipelineId
            NUTRITION_EXPLORE -> recommendationRepository.exploreNutritionPipelineId
            MENTAL_WELLBEING_EXPLORE -> recommendationRepository.explorePhysicalWellbeingPipelineId
            SLEEP_EXPLORE -> recommendationRepository.exploreSleepPipelineId
            PREFERRED_RECOMMENDATIONS -> recommendationRepository.preferredRecommendationsPipelineId
            MOST_POPULAR -> recommendationRepository.mostPopularPipelineId
            NEW_CONTENT -> recommendationRepository.newestContentPipelineId
            STARTING_RECOMMENDATIONS -> recommendationRepository.startingPipelineId
            null -> 0
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
                val feedSectionTypeToUnlock = _viewState.value.data?.feedSectionTypeToUnlock

                feedSections.map { feedSection ->
                    val feedSectionUpdated = if (feedSection.type == feedSectionTypeToUnlock
                        && !isShowingUnlockedSection
                    ) {
                        feedSection.copy(locked = LockType.UNLOCKING)
                    } else {
                        feedSection
                    }
                    FeedSectionAndRecommendations(
                        feedSection = feedSectionUpdated,
                        recommendations = if (feedSectionUpdated.locked != LockType.UNLOCKED) {
                            emptyList()
                        } else {
                            when (feedSectionUpdated.type) {
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
                        }
                    )
                }.filter {
                    it.recommendations.isNotEmpty() || it.feedSection.locked != LockType.UNLOCKED
                }
            }.onStart {
                _viewState.value = _viewState.value.copy(error = null, isLoading = true)
            }.catch { error ->
                _viewState.value = _viewState.value.copy(error = error, isLoading = false)
                logger.e(error)
            }.collectLatest { sections ->
                if (isContentUpdated(_viewState.value.data?.feedSectionTypeToUnlock)) {
                    resetPipelineIds(null)
                    Log.e("XXX", "isContentUpdated=true")
                    _viewState.value.data?.resetScrollTriggerState?.consumed()
                }
                val uiState = _viewState.value
                _viewState.value = uiState.copy(
                    isLoading = forceShowLoading,
                    error = null,
                    data = (uiState.data ?: FeedUI()).copy(sections = sections),
                )
            }
        }
    }
}