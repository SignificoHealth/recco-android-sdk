package com.recco.api.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recco.api.model.ReccoConfig
import com.recco.api.model.ReccoStyle
import com.recco.internal.core.logger.Logger
import com.recco.internal.core.model.metric.AppUserMetricAction
import com.recco.internal.core.model.metric.AppUserMetricCategory
import com.recco.internal.core.model.metric.AppUserMetricEvent
import com.recco.internal.core.repository.AppRepository
import com.recco.internal.core.repository.MeRepository
import com.recco.internal.core.repository.MetricRepository
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
internal class MainViewModel @Inject constructor(
    private val meRepository: MeRepository,
    private val metricRepository: MetricRepository,
    private val appRepository: AppRepository,
    private val logger: Logger
) : ViewModel() {
    private val _viewState = MutableStateFlow(UiState<MainUI>())

    val reccoStyle: ReccoStyle
        get() {
            return _viewState.value.data?.user?.reccoStyle ?: appRepository.getSDKConfig().style
        }

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
            MainUserInteract.ReccoSDKOpen -> onReccoSDKOpen()
        }
    }

    private fun onReccoSDKOpen() {
        metricRepository.logEvent(
            AppUserMetricEvent(
                category = AppUserMetricCategory.USER_SESSION,
                action = AppUserMetricAction.RECCO_SDK_OPEN
            )
        )
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
