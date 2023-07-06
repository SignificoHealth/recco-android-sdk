package com.recco.internal.core.test.utils

import com.recco.internal.core.ui.components.UiState

/**
 * UiState
 */
fun <T> expectedUiStateWithData(data: T) = UiState(
    isLoading = false,
    data = data,
    error = null
)

val expectedWithLoading = UiState(
    isLoading = true,
    data = null,
    error = null
)

val staticThrowableForTesting = IllegalStateException()
val expectedWithError = UiState(
    isLoading = false,
    data = null,
    error = staticThrowableForTesting
)