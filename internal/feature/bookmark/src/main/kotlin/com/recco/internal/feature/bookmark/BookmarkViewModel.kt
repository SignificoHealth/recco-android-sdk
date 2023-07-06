package com.recco.internal.feature.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recco.internal.core.logger.Logger
import com.recco.internal.core.repository.RecommendationRepository
import com.recco.internal.core.ui.components.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val recommendationRepository: RecommendationRepository,
    private val logger: Logger
) : ViewModel() {
    private val _viewState = MutableStateFlow(UiState<BookmarkUI>())
    val viewState: StateFlow<UiState<BookmarkUI>> = _viewState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = UiState()
    )

    init {
        initialLoadOrRetry()
    }

    internal fun onUserInteract(userInteract: BookmarkUserInteract) {
        when (userInteract) {
            BookmarkUserInteract.Retry -> initialLoadOrRetry()
            BookmarkUserInteract.Refresh -> refresh()
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _viewState.emit(viewState.value.copy(error = null, isLoading = true))
            recommendationRepository.reloadBookmarks()
        }
    }

    private fun initialLoadOrRetry() {
        viewModelScope.launch {
            recommendationRepository.bookmarks.onStart {
                _viewState.emit(viewState.value.copy(error = null, isLoading = true))
            }.catch { error ->
                _viewState.emit(_viewState.value.copy(error = error, isLoading = false))
                logger.e(error)
            }.collectLatest { recommendations ->
                val data = (_viewState.value.data ?: BookmarkUI())
                _viewState.emit(
                    _viewState.value.copy(
                        isLoading = false,
                        error = null,
                        data = data.copy(recommendations = recommendations),
                    )
                )
            }
        }
    }
}
