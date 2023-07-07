package com.recco.internal.core.test.extensions

import com.recco.internal.core.ui.components.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent

/**
 * @param viewState the UIState flow.
 * @param eventsToDrop avoids collecting Loading or initialSubscribe events when needed.
 * @param runInteractions defines the userInteractions taking place.
 * @return events the collector of the UIState upon with perform assertions.
 */
@ExperimentalCoroutinesApi
fun <T> TestScope.onViewModelInteraction(
    viewState: Flow<UiState<T>>,
    eventsToDrop: Int,
    runInteractions: () -> Unit
): MutableList<UiState<T>> {
    val events = mutableListOf<UiState<T>>()

    viewState
        .drop(eventsToDrop)
        .onEach(events::add)
        .launchIn(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))
        .invokeOnCompletion { cancel() }
    runInteractions()
    runCurrent()

    return events
}