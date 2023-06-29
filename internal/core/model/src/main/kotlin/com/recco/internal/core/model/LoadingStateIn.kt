package com.recco.internal.core.model

sealed class LoadingStateIn<out T : Any> {
    object Loading : LoadingStateIn<Nothing>()
    data class Success<out T : Any>(val data: T) : LoadingStateIn<T>()
}
