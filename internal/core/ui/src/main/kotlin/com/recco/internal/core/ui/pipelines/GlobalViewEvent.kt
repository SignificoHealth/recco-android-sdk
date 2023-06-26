package com.recco.internal.core.ui.pipelines

import android.os.Bundle
import androidx.annotation.StringRes
import com.recco.internal.core.model.feed.FeedSectionState
import com.recco.internal.core.model.feed.FeedSectionType
import com.recco.internal.core.model.feed.Topic
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.components.ToastType
import com.recco.internal.core.ui.extensions.asDescriptionRes
import com.recco.internal.core.ui.extensions.asTitleRes
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
        val type: ToastType,
        val navDestId: Int? = null,
        val navArgs: Bundle? = null
    ) : GlobalViewEvent()

    data class FeedSectionToUnlock(
        val topic: Topic,
        val type: FeedSectionType,
        val state: FeedSectionState
    ) : GlobalViewEvent()

    // Initialization with no operation purposes GlobalViewEvent
    object NoOp
}

/**
 * Unifies error toast element generation in different screens.
 */
fun showErrorToast(error: Throwable) = GlobalViewEvent.ShowToast(
    titleRes = error.asTitleRes(),
    subtitleRes = error.asDescriptionRes(),
    type = ToastType.Error
)
