package com.shadowflight.uicommons.extensions

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.shadowflight.model.exceptions.InternalServerErrorException
import com.shadowflight.model.exceptions.NoConnectException
import com.shadowflight.model.exceptions.ServiceUnavailableException
import com.shadowflight.uicommons.R

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
fun Throwable?.asDrawableRes(): Int = when (this) {
    is NoConnectException -> R.drawable.bg_no_connection
    else -> R.drawable.bg_no_connection
}
