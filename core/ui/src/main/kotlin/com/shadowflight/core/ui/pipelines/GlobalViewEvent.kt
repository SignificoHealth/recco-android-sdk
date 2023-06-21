package com.shadowflight.core.ui.pipelines

import android.os.Bundle
import androidx.annotation.StringRes
import com.shadowflight.core.model.feed.FeedSectionState
import com.shadowflight.core.model.feed.FeedSectionType
import com.shadowflight.core.model.feed.Topic
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

    data class FeedSectionUnlock(
        val topic: Topic,
        val feedSectionType: FeedSectionType,
        val feedSectionState: FeedSectionState
    ) : GlobalViewEvent()
}

enum class ToastMessageType {
    Reward, Confirmation, Error
}
