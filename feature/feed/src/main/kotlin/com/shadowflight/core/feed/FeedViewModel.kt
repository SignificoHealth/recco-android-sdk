package com.shadowflight.core.feed

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
import com.shadowflight.core.model.feed.Topic
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

private const val DELAY_TO_PERFORM_SCROLL_ANIM = 500L

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val recommendationRepository: RecommendationRepository,
    private val feedApiObserver: FeedApiObserver,
    private val logger: Logger
) : ViewModel() {
    private val _viewState = MutableStateFlow(UiState<FeedUI>())
    val viewState: StateFlow<UiState<FeedUI>> = _viewState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = UiState()
    )
    private var forceShowLoading = false

    init {
        setUpGlobalViewEvents()
        initialLoadOrRetry()
    }

    fun onUserInteract(userInteract: FeedUserInteract) {
        when (userInteract) {
            FeedUserInteract.Retry -> initialLoadOrRetry()
            FeedUserInteract.Refresh -> refresh()
            FeedUserInteract.RefreshUnlockedFeedSection -> hideLoadingAndClearUnlockingState()
        }
    }

    private fun hideLoadingAndClearUnlockingState() {
        viewModelScope.launch {
            val feedSectionType = _viewState.value.data?.feedSectionTypeToUnlock
            forceShowLoading = false
            _viewState.value = _viewState.value.copy(
                isLoading = forceShowLoading,
                data = _viewState.value.data?.copy(feedSectionTypeToUnlock = null),
            )
            feedRepository.setFeedSectionAsUnlocked(feedSectionType)
        }
    }

    private fun setUpGlobalViewEvents() {
        viewModelScope.launch {
            globalViewEvents
                .filter { it is GlobalViewEvent.ResetFeedScroll }
                .collectLatest {
                    val (topic, feedSectionType) = (it as GlobalViewEvent.ResetFeedScroll)
                    feedApiObserver.reset()
                    moveUnlockedFeedSectionAtTop(feedSectionType)
                    delay(DELAY_TO_PERFORM_SCROLL_ANIM)
                    forceLoadingWhileRefreshingFeedSection(topic!!)
                }
        }
    }

    private fun moveUnlockedFeedSectionAtTop(feedSectionType: FeedSectionType?) {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(
                data = _viewState.value.data?.copy(
                    feedSectionTypeToUnlock = feedSectionType,
                    resetScrollTriggerState = TriggerState().apply { trigger() }
                ),
            )
            feedRepository.moveUnlockedFeedSectionAtTop(feedSectionType!!)
        }
    }

    private fun forceLoadingWhileRefreshingFeedSection(topic: Topic) {
        viewModelScope.launch {
            forceShowLoading = true
            _viewState.value = _viewState.value.copy(
                isLoading = forceShowLoading,
            )
            feedRepository.reloadFeed()
            recommendationRepository.reloadSection(topic = topic)
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
                val feedSectionTypeToUnlock = _viewState.value.data?.feedSectionTypeToUnlock

                feedSections.map { feedSection ->
                    if (feedSection.type == feedSectionTypeToUnlock) {
                        feedSection.copy(locked = LockType.UNLOCKING)
                    } else {
                        feedSection
                    }
                }.map { feedSection ->
                    FeedSectionAndRecommendations(
                        feedSection = feedSection,
                        recommendations = if (feedSection.locked != LockType.UNLOCKED) {
                            emptyList()
                        } else {
                            when (feedSection.type) {
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
                val data = (_viewState.value.data ?: FeedUI())
                val triggerStateUpdated =
                    if (feedApiObserver.hasFinished(data.feedSectionTypeToUnlock)) {
                        data.resetScrollTriggerState.also { it.consumed() }
                    } else {
                        data.resetScrollTriggerState
                    }

                _viewState.value = _viewState.value.copy(
                    isLoading = forceShowLoading,
                    error = null,
                    data = data.copy(
                        resetScrollTriggerState = triggerStateUpdated,
                        sections = sections
                    ),
                )
            }
        }
    }
}