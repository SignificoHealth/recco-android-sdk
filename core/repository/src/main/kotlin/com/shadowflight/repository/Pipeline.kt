package com.shadowflight.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class Pipeline<T>(val loadOrReload: suspend () -> T) {
    private val _state = MutableStateFlow<T?>(null)
    var value = _state.value
        private set

    val state: Flow<T> =
        _state.map {
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
        value = loadOrReload()
        _state.emit(value)
    }

    fun clearValue() {
        value = null
    }
}