package com.recco.internal.core.ui.notifications

import android.Manifest
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher

fun ManagedActivityResultLauncher<String, Boolean>.askForNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}
