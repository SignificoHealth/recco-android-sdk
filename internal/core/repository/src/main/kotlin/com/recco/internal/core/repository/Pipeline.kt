package com.recco.internal.core.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlin.random.Random

internal class Pipeline<T>(private val remoteDatasource: suspend () -> T) {
    private val _state = MutableStateFlow(PipelineUpdate(remoteDatasource))
    var value: T? = null
        private set

    val state: Flow<T> = _state.map { update ->
        when {
            value != null -> value!!
            else -> {
                update.datasource().also { updated ->
                    value = updated
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
        _state.emit(PipelineUpdate(remoteDatasource))
    }

    suspend fun replaceWithLocal(data: T) {
        clearValue()
        _state.emit(PipelineUpdate(datasource = { data }))
    }

    private fun clearValue() {
        value = null
    }
}

private data class PipelineUpdate<T>(
    val datasource: suspend () -> T,
    val id: Int = Random.nextInt()
)