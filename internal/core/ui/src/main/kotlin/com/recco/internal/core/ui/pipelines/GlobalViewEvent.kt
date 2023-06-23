package com.recco.internal.core.ui.pipelines

import android.os.Bundle
import androidx.annotation.StringRes
import com.recco.internal.core.model.feed.FeedSectionState
import com.recco.internal.core.model.feed.FeedSectionType
import com.recco.internal.core.model.feed.Topic
import kotlinx.coroutines.flow.MutableSharedFlow
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

    data class FeedSectionToUnlock(
        val topic: Topic,
        val type: FeedSectionType,
        val state: FeedSectionState
    ) : GlobalViewEvent()
}

enum class ToastMessageType {
    Reward, Confirmation, Error
}