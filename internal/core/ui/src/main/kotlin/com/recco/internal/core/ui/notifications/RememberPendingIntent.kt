package com.recco.internal.core.ui.notifications

import android.app.PendingIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode

@Composable
fun rememberPendingIntent(): PendingIntent? {
    val context = LocalContext.current
    val isInPreviewMode = LocalInspectionMode.current

    return remember(context) {
        if (!isInPreviewMode) {
            PendingIntent.getActivity(
                context,
                0,
                context.packageManager.getLaunchIntentForPackage(context.packageName),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            null
        }
    }
}
