package com.shadowflight.core.ui.pipelines

import android.os.Bundle
import androidx.annotation.StringRes
import java.util.UUID

sealed class ViewEvent {
    data class ShowToast(
        @StringRes val titleRes: Int,
        val titleArgs: List<Any> = emptyList(),
        @StringRes val subtitleRes: Int? = null,
        val type: ToastMessageType,
        val navDestId: Int? = null,
        val navArgs: Bundle? = null,
        val id: UUID = UUID.randomUUID(),
    ) : ViewEvent()
}

enum class ToastMessageType {
    Reward, Confirmation, Error
}
