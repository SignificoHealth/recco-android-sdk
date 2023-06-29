package com.recco.internal.core.repository

import com.recco.internal.core.model.LoadingStateIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

internal class PipelineLoading<T : Any>(
    private val coroutineScope: CoroutineScope,
    private val remoteDatasource: suspend () -> T,
) {
    private val _state = MutableStateFlow(PipelineLoadingUpdate(remoteDatasource))
    var value: LoadingStateIn<T> = LoadingStateIn.Loading
        private set

    val state: Flow<LoadingStateIn<T>> = _state.map { update ->
        when {
            value != LoadingStateIn.Loading -> value
            else -> {
                update.datasource().let { updated ->
                    value = LoadingStateIn.Success(updated)
                    LoadingStateIn.Success(updated)
                }
            }
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = LoadingStateIn.Loading
    )

    suspend fun reloadRemoteDatasource() {
        // StateFlow does not emit same value twice, thus we emit a dummy value to allow emitting again
        // even if the loaded data is the same it was before in the latest emission.
        // This also allow us to call loadOrReload inside the stream, catching any potential exception
        // as part of the Flow stream.
        clearValue()
        _state.emit(PipelineLoadingUpdate(remoteDatasource))
    }

    suspend fun replaceWithLocal(data: T) {
        clearValue()
        _state.emit(PipelineLoadingUpdate(datasource = { data }))
    }

    private fun clearValue() {
        value = LoadingStateIn.Loading
    }
}

private data class PipelineLoadingUpdate<T>(
    val datasource: suspend () -> T,
    val id: Int = Random.nextInt()
)