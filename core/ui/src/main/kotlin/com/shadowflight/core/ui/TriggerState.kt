package com.shadowflight.core.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

class TriggerState {
    private var pendingToConsume: Boolean by mutableStateOf(false)

    fun trigger() {
        pendingToConsume = true
    }

    fun consumed() {
        pendingToConsume = false
    }

    suspend fun consumedDelayed() {
        delay(DELAY)
        consumed()
    }

    fun isPendingToConsume(): Boolean = pendingToConsume

    companion object {
        private const val DELAY = 1000L
    }
}
