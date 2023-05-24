package com.shadowflight.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.shadowflight.ui.MainUiState.Loading
import com.shadowflight.ui.MainUiState.Success
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

sealed interface MainUiState {
    object Loading : MainUiState
    object Success : MainUiState
}

@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {
    val uiState = flow<MainUiState> { emit(Success) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = Loading
    )
}
