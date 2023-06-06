package com.shadowflight.core.ui.pipelines

import kotlinx.coroutines.flow.MutableSharedFlow

object Pipelines {
    val viewEvents = MutableSharedFlow<ViewEvent>()
}