package com.shadowflight.core.ui.models

data class UiState<T>(
    val isLoading: Boolean = true,
    val error: Throwable? = null,
    val data: T? = null
)