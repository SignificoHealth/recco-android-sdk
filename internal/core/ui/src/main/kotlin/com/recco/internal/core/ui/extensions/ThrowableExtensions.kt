package com.recco.internal.core.ui.extensions

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.recco.internal.core.model.exceptions.InternalServerErrorException
import com.recco.internal.core.model.exceptions.NoConnectException
import com.recco.internal.core.model.exceptions.ServiceUnavailableException
import com.recco.internal.core.ui.R
import com.recco.internal.core.ui.components.AppTintedImageNoConnection

@StringRes
fun Throwable?.asTitleRes(): Int = when (this) {
    is NoConnectException -> R.string.recco_error_connection_title
    else -> R.string.recco_error_connection_title
}

@StringRes
fun Throwable?.asDescriptionRes(): Int = when (this) {
    is NoConnectException -> R.string.recco_error_connection_body
    is ServiceUnavailableException,
    is InternalServerErrorException -> R.string.recco_error_connection_body
    else -> R.string.recco_error_connection_body
}

@StringRes
fun Throwable?.asCtaTextRes(): Int = when (this) {
    is NoConnectException -> R.string.recco_error_reload
    else -> R.string.recco_error_reload
}

@DrawableRes
fun Throwable?.asCtaIconRes(): Int = when (this) {
    is NoConnectException -> R.drawable.recco_ic_retry
    else -> R.drawable.recco_ic_retry
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
