package com.shadowflight.core.ui.extensions

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.shadowflight.core.model.exceptions.InternalServerErrorException
import com.shadowflight.core.model.exceptions.NoConnectException
import com.shadowflight.core.model.exceptions.ServiceUnavailableException
import com.shadowflight.core.ui.AppTintedImageNoConnection
import com.shadowflight.core.ui.AppTintedImagePottedPlant2
import com.shadowflight.core.ui.R

@StringRes
fun Throwable?.asTitleRes(): Int = when (this) {
    is NoConnectException -> R.string.no_network_connection_error_title
    else -> R.string.common_error_title
}

@StringRes
fun Throwable?.asDescriptionRes(): Int = when (this) {
    is NoConnectException -> R.string.no_network_connection_error_desc
    is ServiceUnavailableException,
    is InternalServerErrorException -> R.string.server_error
    else -> R.string.common_error_desc
}

@StringRes
fun Throwable?.asCtaTextRes(): Int = when (this) {
    is NoConnectException -> R.string.retry
    else -> R.string.retry
}

@DrawableRes
fun Throwable?.asCtaIconRes(): Int = when (this) {
    is NoConnectException -> R.drawable.ic_retry
    else -> R.drawable.ic_retry
}

@DrawableRes
fun Throwable?.asDrawableRes(): Int? = when (this) {
    is NoConnectException -> null
    else -> null
}

@Composable
fun Throwable?.asDrawableComposable(): @Composable (() -> Unit)? = when (this) {
    is NoConnectException -> {
        { AppTintedImageNoConnection() }
    }
    else -> {
        { AppTintedImageNoConnection() }
    }
}
