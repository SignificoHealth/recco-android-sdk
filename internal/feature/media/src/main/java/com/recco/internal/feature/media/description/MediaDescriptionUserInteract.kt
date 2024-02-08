package com.recco.internal.feature.media.description

internal sealed class MediaDescriptionUserInteract {
    data object Retry : MediaDescriptionUserInteract()
    data object InitialLoad : MediaDescriptionUserInteract()
    data class DontShowWarningDialogChecked(val isChecked: Boolean) : MediaDescriptionUserInteract()
    object OnWarningDialogDismiss : MediaDescriptionUserInteract()
}
