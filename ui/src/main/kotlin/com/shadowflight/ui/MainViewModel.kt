package com.shadowflight.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadowflight.core.logger.Logger
import com.shadowflight.core.repository.MeRepository
import com.shadowflight.core.ui.components.UiState
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
class MainViewModel @Inject constructor(
    private val meRepository: MeRepository,
    private val logger: Logger
) : ViewModel() {
    private val _viewState = MutableStateFlow(UiState<MainUI>())

    val viewState: StateFlow<UiState<MainUI>> = _viewState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = UiState()
    )

    init {
        initialLoadOrRetry()
    }

    fun onUserInteract(userInteract: MainUserInteract) {
        when (userInteract) {
            MainUserInteract.Retry -> initialLoadOrRetry()
        }
    }

    private fun initialLoadOrRetry() {
        viewModelScope.launch {
            meRepository.me
                .onStart {
                    _viewState.value = _viewState.value.copy(error = null, isLoading = false)
                }.catch { error ->
                    _viewState.value = _viewState.value.copy(error = error, isLoading = false)
                    logger.e(error)
                }
                .collectLatest { user ->
                    val uiState = _viewState.value
                    _viewState.value = _viewState.value.copy(
                        isLoading = false,
                        error = null,
                        data = (uiState.data ?: MainUI()).copy(user = user)
                    )
                }
        }
    }
}