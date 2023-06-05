package com.shadowflight.article

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadowflight.article.navigation.idArg
import com.shadowflight.core.logger.Logger
import com.shadowflight.core.model.recommendation.Article
import com.shadowflight.core.model.recommendation.ContentId
import com.shadowflight.repository.RecommendationRepository
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
        initialLoadSubscribe()
    }

    fun onUserInteract(userInteract: ArticleUserInteract) {
        when (userInteract) {
            ArticleUserInteract.Retry -> initialLoadSubscribe()
        }
    }

    private fun initialLoadSubscribe() {
        println(articleId)

        viewModelScope.launch {
            runCatching {
                _viewState.emit(
                    ArticleViewUIState(
                        isLoading = false,
                        article = recommendationRepository.getArticle(articleId)
                    )
                )
            }.onFailure {
                _viewState.emit(ArticleViewUIState(isError = true, isLoading = false))
                logger.e(it)
            }
        }
    }
}

data class ArticleViewUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val article: Article? = null
)