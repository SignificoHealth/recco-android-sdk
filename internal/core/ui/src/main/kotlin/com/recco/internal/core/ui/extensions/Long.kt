package com.recco.internal.core.ui.extensions

import android.text.format.DateUtils

fun Long.formatElapsedTime(): String {
    return DateUtils.formatElapsedTime(this / 1000)
}
