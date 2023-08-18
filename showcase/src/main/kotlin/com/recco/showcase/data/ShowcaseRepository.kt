package com.recco.showcase.data

import android.content.Context
import com.recco.api.model.ReccoFont
import com.recco.api.model.ReccoPalette
import com.recco.showcase.data.entities.ShowcasePaletteEntity
import com.recco.showcase.data.mappers.FRESH_PALETTE_ID
import com.recco.showcase.data.mappers.asReccoPalette
import com.recco.showcase.data.mappers.asShowcasePalette
import com.recco.showcase.data.mappers.asShowcasePaletteEntity
import com.recco.showcase.data.room.PaletteDao
import com.recco.showcase.models.ShowcasePalette
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ShowcaseRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val paletteDao: PaletteDao
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

    fun setSelectedPaletteId(id: Int) {
        with(prefs.edit()) {
            putInt(RECCO_PALETTE_ID_KEY, id)
            apply()
        }
    }

    fun getSelectedPaletteId(): Int = prefs.getInt(RECCO_PALETTE_ID_KEY, FRESH_PALETTE_ID)

    suspend fun getSelectedReccoPalette(): ReccoPalette =
        getPalettes().first { it.id == getSelectedPaletteId() }
            .asReccoPalette()

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

    suspend fun getPalettes() = paletteDao.getAll().map(ShowcasePaletteEntity::asShowcasePalette)
        .plus(
            listOf(ReccoPalette.Fresh, ReccoPalette.Ocean, ReccoPalette.Spring, ReccoPalette.Tech)
                .map { it.asShowcasePalette() }
        )

    suspend fun editPalette(showcasePalette: ShowcasePalette) {
        paletteDao.add(showcasePalette.asShowcasePaletteEntity())
    }

    suspend fun updatePalette(showcasePalette: ShowcasePalette) {
        paletteDao.update(showcasePalette.asShowcasePaletteEntity())
    }

    companion object {
        private const val USER_ID_KEY = "user_id_key"
        private const val RECCO_PALETTE_ID_KEY = "recco_palette_id_key"
        private const val RECCO_FONT_KEY = "recco_font_key"
    }
}
