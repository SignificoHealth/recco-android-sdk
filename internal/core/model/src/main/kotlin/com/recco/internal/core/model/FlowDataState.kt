package com.recco.internal.core.model

sealed class FlowDataState<out T : Any> {
    object Loading : FlowDataState<Nothing>()
    data class Success<out T : Any>(val data: T) : FlowDataState<T>()
}
