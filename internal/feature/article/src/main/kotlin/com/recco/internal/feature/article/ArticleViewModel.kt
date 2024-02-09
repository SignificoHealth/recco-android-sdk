package com.recco.internal.feature.article

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recco.internal.core.logger.Logger
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.ContentType
import com.recco.internal.core.model.recommendation.UserInteractionRecommendation
import com.recco.internal.core.repository.RecommendationRepository
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.feature.article.navigation.idArg
import com.recco.internal.feature.rating.delegates.ContentUserInteract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ArticleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recommendationRepository: RecommendationRepository,
    private val logger: Logger,
    private val contentInteractViewModelDelegate: ContentInteractViewModelDelegate
) : ViewModel() {
    private val articleId by lazy { checkNotNull(savedStateHandle.get<ContentId>(idArg)) }
    private val _viewState = MutableStateFlow(UiState<ArticleUI>())
    val viewState: Flow<UiState<ArticleUI>> = _viewState
    val interactionViewState: Flow<UserInteractionRecommendation?> =
        contentInteractViewModelDelegate.viewState

    init {
        initialLoadSubscribe()
    }

    fun onContentUserInteract(userInteract: ContentUserInteract) {
        contentInteractViewModelDelegate.onContentUserInteract(userInteract)
    }

    fun onUserInteract(userInteract: ArticleUserInteract) {
        when (userInteract) {
            ArticleUserInteract.Retry -> initialLoadSubscribe()
        }
    }

    private fun initialLoadSubscribe() {
        viewModelScope.launch {
            runCatching { recommendationRepository.getArticle(articleId) }
                .onSuccess { article ->
                    contentInteractViewModelDelegate.userInteraction = UserInteractionRecommendation(
                        contentId = articleId,
                        contentType = ContentType.ARTICLE,
                        rating = article.rating,
                        isBookmarked = article.isBookmarked
                    )

                    _viewState.emit(
                        UiState(isLoading = false, data = ArticleUI(article))
                    )
                }.onFailure {
                    _viewState.emit(UiState(error = it, isLoading = false))
                    logger.e(it)
                }
        }
    }

    override fun onCleared() {
        contentInteractViewModelDelegate.onCleared()
        super.onCleared()
    }
}
