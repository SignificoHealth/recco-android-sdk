package com.recco.internal.core.ui.extensions

import androidx.core.text.HtmlCompat

// TODO remove this when devs from Frontitude fix the issue already reported
fun String.removeHTML() = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()