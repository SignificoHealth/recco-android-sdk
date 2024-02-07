package com.recco.internal.core.persistence

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReccoPreferences @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs by lazy {
        context.getSharedPreferences(
            "com.recco.internal.core.persistence",
            Context.MODE_PRIVATE
        )
    }

    var shouldShowVideoWarningDialog: Boolean
        get() = prefs.getBoolean(KEY_SHOW_VIDEO_WARNING_DIALOG, true)
        set(value) = prefs.edit { putBoolean(KEY_SHOW_VIDEO_WARNING_DIALOG, value) }

    private companion object {
        private const val KEY_SHOW_VIDEO_WARNING_DIALOG = "key_show_video_warning_dialog"
    }
}
