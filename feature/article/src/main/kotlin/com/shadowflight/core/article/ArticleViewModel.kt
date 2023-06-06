package com.shadowflight.core.article

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadowflight.core.article.navigation.idArg
import com.shadowflight.core.logger.Logger
import com.shadowflight.core.model.recommendation.Article
import com.shadowflight.core.model.recommendation.ContentId
import com.shadowflight.core.model.recommendation.Rating
import com.shadowflight.core.repository.RecommendationRepository
import com.shadowflight.core.ui.R
import com.shadowflight.core.ui.pipelines.Pipelines
import com.shadowflight.core.ui.pipelines.ToastMessageType
import com.shadowflight.core.ui.pipelines.ViewEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recommendationRepository: RecommendationRepository,
    private val logger: Logger
) : ViewModel() {
    private val articleId by lazy { checkNotNull(savedStateHandle.get<ContentId>(idArg)) }
    private val _viewState = MutableStateFlow(ArticleViewUIState(isLoading = true))
    val viewState: Flow<ArticleViewUIState> = _viewState

    init {
        setArticleAsSeen()
        initialLoadSubscribe()
    }

    fun onUserInteract(userInteract: ArticleUserInteract) {
        when (userInteract) {
            ArticleUserInteract.Retry -> initialLoadSubscribe()
            ArticleUserInteract.ToggleBookmarkState -> toggleBookmarkState()
            ArticleUserInteract.ToggleLikeState -> {
                toggleRatingState(isLikeLoading = true, newRating = Rating.LIKE)
            }

            ArticleUserInteract.ToggleDislikeState -> {
                toggleRatingState(isDislikeLoading = true, newRating = Rating.DISLIKE)
            }
        }
    }

    private fun setArticleAsSeen() {
        viewModelScope.launch {
            recommendationRepository.setRecommendationAsViewed(
                articleId
            )
        }
    }

    private fun initialLoadSubscribe() {
        viewModelScope.launch {
            runCatching { recommendationRepository.getArticle(articleId) }
                .onSuccess { article ->
                    _viewState.emit(
                        ArticleViewUIState(
                            isLoading = false,
                            article = recommendationRepository.getArticle(articleId),
                            userInteraction = UserInteractionRecommendation(
                                rating = article.rating,
                                isBookmarked = article.isBookmarked
                            )
                        )
                    )
                }.onFailure {
                    _viewState.emit(ArticleViewUIState(isError = true, isLoading = false))
                    logger.e(it)
                }
        }
    }

    private fun toggleBookmarkState() {
        viewModelScope.launch {
            val uiState = _viewState.value
            val article = checkNotNull(uiState.article)
            val userInteraction = checkNotNull(uiState.userInteraction)

            _viewState.emit(
                uiState.copy(userInteraction = userInteraction.copy(isBookmarkLoading = true))
            )

            val newBookmarkedState = !userInteraction.isBookmarked
            runCatching {
                recommendationRepository.setBookmarkRecommendation(
                    contentId = article.id,
                    bookmarked = newBookmarkedState
                )
            }.onSuccess {
                _viewState.emit(
                    uiState.copy(
                        userInteraction = userInteraction.copy(
                            isBookmarkLoading = false,
                            isBookmarked = newBookmarkedState
                        )
                    )
                )
            }.onFailure {
                logger.e(it)
                Pipelines.viewEvents.emit(
                    ViewEvent.ShowToast(
                        titleRes = R.string.common_error_desc,
                        type = ToastMessageType.Error
                    )
                )
            }
        }
    }

    private fun toggleRatingState(
        isDislikeLoading: Boolean = false,
        isLikeLoading: Boolean = false,
        newRating: Rating
    ) {
        viewModelScope.launch {
            val uiState = _viewState.value
            val article = checkNotNull(uiState.article)
            val userInteraction = checkNotNull(uiState.userInteraction)

            _viewState.emit(
                uiState.copy(
                    userInteraction = userInteraction.copy(
                        isDislikeLoading = isDislikeLoading,
                        isLikeLoading = isLikeLoading
                    )
                )
            )

            val isOppositeAction =
                userInteraction.rating == Rating.DISLIKE && newRating == Rating.LIKE
                        || userInteraction.rating == Rating.LIKE && newRating == Rating.DISLIKE
            val newRatingState =
                if (userInteraction.rating == Rating.NOT_RATED || isOppositeAction) {
                    newRating
                } else {
                    Rating.NOT_RATED
                }

            runCatching {
                recommendationRepository.setRecommendationRating(
                    contentId = article.id,
                    rating = newRatingState
                )
            }.onSuccess {
                _viewState.emit(
                    uiState.copy(
                        userInteraction = userInteraction.copy(
                            isDislikeLoading = false,
                            isLikeLoading = false,
                            rating = newRatingState
                        )
                    )
                )
            }.onFailure {
                logger.e(it)
                Pipelines.viewEvents.emit(
                    ViewEvent.ShowToast(
                        titleRes = R.string.common_error_desc,
                        type = ToastMessageType.Error
                    )
                )
            }
        }
    }
}

data class ArticleViewUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val article: Article? = null,
    val userInteraction: UserInteractionRecommendation? = null
)