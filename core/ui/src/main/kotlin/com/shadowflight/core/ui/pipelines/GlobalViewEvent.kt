package com.shadowflight.core.ui.pipelines

import android.os.Bundle
import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID
import kotlin.random.Random

val globalViewEvents = MutableSharedFlow<GlobalViewEvent>()

sealed class GlobalViewEvent(
    // The id property ensures that it is a new value emitted always
    val id: Int = Random.nextInt()
) {
    data class ShowToast(
        @StringRes val titleRes: Int,
        val titleArgs: List<Any> = emptyList(),
        @StringRes val subtitleRes: Int? = null,
        val type: ToastMessageType,
        val navDestId: Int? = null,
        val navArgs: Bundle? = null
    ) : GlobalViewEvent()

    object ResetFeedScroll : GlobalViewEvent()
}

enum class ToastMessageType {
    Reward, Confirmation, Error
}
