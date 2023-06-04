package com.shadowflight.core.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlin.random.Random

class Pipeline<T>(val loadOrReload: suspend () -> T) {
    private val _state = MutableStateFlow(PipelineUpdate())
    var value: T? = null
        private set

    val state: Flow<T> = _state.map {
        when {
            value != null -> value!!
            else -> {
                loadOrReload().also { updated ->
                    value = updated
                }
            }
        }
    }

    suspend fun update() {
        // StateFlow does not emit same value twice, thus we emit a dummy value to allow emitting again
        // even if the loaded data is the same it was before in the latest emission.
        // This also allow us to call loadOrReload inside the stream, catching any potential exception
        // as part of the Flow stream.
        value = null
        _state.emit(PipelineUpdate())
    }

    fun clearValue() {
        value = null
    }
}

private data class PipelineUpdate(val id: Int = Random.nextInt())