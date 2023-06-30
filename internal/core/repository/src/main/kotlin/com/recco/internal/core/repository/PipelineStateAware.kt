package com.recco.internal.core.repository

import com.recco.internal.core.model.FlowDataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlin.random.Random

internal class PipelineStateAware<T : Any>(private val remoteDatasource: suspend () -> T) {
    private val _state = MutableStateFlow<PipelineState<T>>(PipelineState.InitialLoading)
    var value: FlowDataState<T> = FlowDataState.Loading
        private set

    val state: Flow<FlowDataState<T>> = _state.map { update ->
        if (update is PipelineState.InitialLoading) {
            FlowDataState.Loading.also {
                _state.emit(PipelineState.Update(datasource = remoteDatasource))
            }
        } else {
            when {
                value != FlowDataState.Loading -> value
                else -> {
                    (update as PipelineState.Update).datasource().let { updated ->
                        value = FlowDataState.Success(updated)
                        FlowDataState.Success(updated)
                    }
                }
            }
        }
    }

    suspend fun reloadRemoteDatasource() {
        // StateFlow does not emit same value twice, thus we emit a dummy value to allow emitting again
        // even if the loaded data is the same it was before in the latest emission.
        // This also allow us to call loadOrReload inside the stream, catching any potential exception
        // as part of the Flow stream.
        clearValue()
        _state.emit(PipelineState.Update(remoteDatasource))
    }

    suspend fun replaceWithLocal(data: T) {
        clearValue()
        _state.emit(PipelineState.Update(datasource = { data }))
    }

    private fun clearValue() {
        value = FlowDataState.Loading
    }

    private sealed class PipelineState<out T : Any> {
        object InitialLoading : PipelineState<Nothing>()

        data class Update<out T : Any>(
            val datasource: suspend () -> T,
            val id: Int = Random.nextInt(),
        ) : PipelineState<T>()
    }
}