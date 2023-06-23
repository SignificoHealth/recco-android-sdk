package com.recco.internal.core.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

class TriggerState {
    private var pendingToConsume: Boolean? by mutableStateOf(null)

    fun trigger(onlyOnce: Boolean = true) {
        if (!onlyOnce || pendingToConsume == null) {
            pendingToConsume = true
        }
    }

    fun consumed() {
        pendingToConsume = false
    }

    suspend fun consumedDelayed() {
        delay(DELAY)
        consumed()
    }

    fun isPendingToConsume(): Boolean = pendingToConsume == true

    fun isConsumed(): Boolean = pendingToConsume == false

    companion object {
        private const val DELAY = 700L
    }
}
