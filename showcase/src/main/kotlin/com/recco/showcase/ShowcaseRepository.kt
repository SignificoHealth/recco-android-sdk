package com.recco.showcase

import android.content.Context
import com.recco.api.model.ReccoFont
import com.recco.api.model.ReccoPalette
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ShowcaseRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs by lazy {
        context.getSharedPreferences("showcase", Context.MODE_PRIVATE)
    }

    fun setUserId(userId: String) {
        with(prefs.edit()) {
            putString(USER_ID_KEY, userId)
            apply()
        }
    }

    fun clearUserId() {
        with(prefs.edit()) {
            putString(USER_ID_KEY, null)
            apply()
        }
    }

    fun isUserLoggedIn() = !prefs.getString(USER_ID_KEY, "").isNullOrBlank()

    fun setReccoPalette(palette: ReccoPalette) {
        with(prefs.edit()) {
            putString(RECCO_PALETTE_KEY, palette.asName())
            apply()
        }
    }

    fun getSelectedReccoPalette(): ReccoPalette {
        val reccoPaletteName = prefs.getString(RECCO_PALETTE_KEY, "")
        if (reccoPaletteName.isNullOrBlank()) return ReccoPalette.Fresh
        return reccoPaletteName.asPalette()
    }

    fun setReccoFont(font: ReccoFont) {
        with(prefs.edit()) {
            putString(RECCO_FONT_KEY, font.fontName)
            apply()
        }
    }

    fun getSelectedReccoFont(): ReccoFont {
        val fontName = prefs.getString(RECCO_FONT_KEY, "")
        return if (fontName.isNullOrBlank()) {
            ReccoFont.POPPINS
        } else {
            ReccoFont.values().first { it.fontName == fontName }
        }
    }

    private fun ReccoPalette.asName(): String = when (this) {
        is ReccoPalette.Custom -> TODO()
        ReccoPalette.Fresh -> "Fresh"
        ReccoPalette.Ocean -> "Ocean"
        ReccoPalette.Spring -> "Spring"
        ReccoPalette.Tech -> "Tech"
    }

    private fun String.asPalette() = when (this) {
        "Fresh" -> ReccoPalette.Fresh
        "Ocean" -> ReccoPalette.Ocean
        "Spring" -> ReccoPalette.Spring
        "Tech" -> ReccoPalette.Tech
        else -> throw IllegalArgumentException("$this is not a supported palette")
    }

    companion object {
        private const val USER_ID_KEY = "user_id_key"
        private const val RECCO_PALETTE_KEY = "recco_palette_key"
        private const val RECCO_FONT_KEY = "recco_font_key"
    }
}
