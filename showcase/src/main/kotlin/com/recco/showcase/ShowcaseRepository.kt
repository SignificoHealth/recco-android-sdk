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
        return if (reccoPaletteName.isNullOrBlank()) {
            ReccoPalette.Fresh
        } else {
            reccoPaletteName.asPalette()
        }
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
        ReccoPalette.Fresh -> FRESH_PALETTE
        ReccoPalette.Ocean -> OCEAN_PALETTE
        ReccoPalette.Spring -> SPRING_PALETTE
        ReccoPalette.Tech -> TECH_PALETTE
    }

    private fun String.asPalette() = when (this) {
        FRESH_PALETTE -> ReccoPalette.Fresh
        OCEAN_PALETTE -> ReccoPalette.Ocean
        SPRING_PALETTE -> ReccoPalette.Spring
        TECH_PALETTE -> ReccoPalette.Tech
        else -> throw IllegalArgumentException("$this is not a supported palette")
    }

    companion object {
        private const val USER_ID_KEY = "user_id_key"
        private const val RECCO_PALETTE_KEY = "recco_palette_key"
        private const val RECCO_FONT_KEY = "recco_font_key"
        private const val FRESH_PALETTE = "Fresh"
        private const val OCEAN_PALETTE = "Ocean"
        private const val SPRING_PALETTE = "Spring"
        private const val TECH_PALETTE = "Tech"
    }
}
