package com.recco.internal.core.ui.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat

fun Context.openUrlInBrowser(url: String) {
    runCatching {
        ContextCompat.startActivity(this, Intent(Intent.ACTION_VIEW, Uri.parse(url)), null)
    }
}