package com.recco.internal.feature.article

import com.recco.internal.core.logger.Logger
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.UserInteractionRecommendation
import com.recco.internal.core.repository.RecommendationRepository
import com.recco.internal.core.ui.pipelines.globalViewEvents
import com.recco.internal.core.ui.pipelines.showErrorToast
import com.recco.internal.feature.rating.delegates.ContentUserInteract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContentInteractViewModelDelegate @Inject constructor(
    private val recommendationRepository: RecommendationRepository,
    private val logger: Logger
) {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)
    private val _viewState = MutableStateFlow<UserInteractionRecommendation?>(null)
    val viewState = _viewState.asStateFlow()

    var userInteraction: UserInteractionRecommendation?
        get() = _viewState.value
        set(value) {
            _viewState.value = value

            scope.launch {
                value?.contentId?.let {
                    recommendationRepository.setRecommendationAsViewed(it)
                }
            }
        }

    fun onContentUserInteract(userInteract: ContentUserInteract) {
        when (userInteract) {
            is ContentUserInteract.ToggleBookmarkState -> {
                toggleBookmarkState(userInteract.contentId)
            }
            is ContentUserInteract.ToggleLikeState -> {
                toggleRatingState(
                    contentId = userInteract.contentId,
                    isLikeLoading = true,
                    newRating = Rating.LIKE
                )
            }
            is ContentUserInteract.ToggleDislikeState -> {
                toggleRatingState(
                    contentId = userInteract.contentId,
                    isDislikeLoading = true,
                    newRating = Rating.DISLIKE
                )
            }
        }
    }

    private fun toggleBookmarkState(contentId: ContentId) {
        scope.launch {
            val userInteraction = checkNotNull(userInteraction)

            _viewState.emit(userInteraction.copy(isBookmarkLoading = true))

            val newBookmarkedState = !userInteraction.isBookmarked
            runCatching {
                recommendationRepository.setBookmarkRecommendation(
                    contentId = contentId,
                    contentType = userInteraction.contentType,
                    bookmarked = newBookmarkedState
                )
            }.onSuccess {
                _viewState.update {
                    checkNotNull(it).copy(
                        isBookmarkLoading = false,
                        isBookmarked = newBookmarkedState
                    )
                }
            }.onFailure {
                logger.e(it)
                globalViewEvents.emit(showErrorToast(it))
            }
        }
    }

    private fun toggleRatingState(
        contentId: ContentId,
        isDislikeLoading: Boolean = false,
        isLikeLoading: Boolean = false,
        newRating: Rating
    ) {
        scope.launch {
            _viewState.update {
                it?.copy(
                    isDislikeLoading = isDislikeLoading,
                    isLikeLoading = isLikeLoading
                )
            }

            val userInteraction = checkNotNull(
                this@ContentInteractViewModelDelegate.userInteraction
            )

            val isOppositeAction =
                userInteraction.rating == Rating.DISLIKE && newRating == Rating.LIKE ||
                    userInteraction.rating == Rating.LIKE && newRating == Rating.DISLIKE
            val newRatingState =
                if (userInteraction.rating == Rating.NOT_RATED || isOppositeAction) {
                    newRating
                } else {
                    Rating.NOT_RATED
                }

            runCatching {
                recommendationRepository.setRecommendationRating(
                    contentId = contentId,
                    contentType = userInteraction.contentType,
                    rating = newRatingState
                )
            }.onSuccess {
                _viewState.update {
                    checkNotNull(it).copy(
                        isDislikeLoading = false,
                        isLikeLoading = false,
                        rating = newRatingState
                    )
                }
            }.onFailure {
                logger.e(it)
                globalViewEvents.emit(showErrorToast(it))
            }
        }
    }

    fun onCleared() {
        job.cancel()
    }
}
