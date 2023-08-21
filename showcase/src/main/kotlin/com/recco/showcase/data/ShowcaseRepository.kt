package com.recco.showcase.data

import android.app.Application
import android.content.Context
import com.recco.api.model.ReccoFont
import com.recco.api.model.ReccoPalette
import com.recco.api.model.ReccoStyle
import com.recco.showcase.ShowcaseApp
import com.recco.showcase.data.entities.ShowcasePaletteEntity
import com.recco.showcase.data.mappers.FRESH_PALETTE_ID
import com.recco.showcase.data.mappers.asReccoPalette
import com.recco.showcase.data.mappers.asShowcasePalette
import com.recco.showcase.data.mappers.asShowcasePaletteEntity
import com.recco.showcase.data.room.PaletteDao
import com.recco.showcase.models.ShowcasePalette
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ShowcaseRepository @Inject constructor(
    @ApplicationContext private val context: Context,
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

    suspend fun setSelectedPaletteId(id: Int) {
        with(prefs.edit()) {
            putInt(RECCO_PALETTE_ID_KEY, id)
            apply()
        }

        ShowcaseApp.initSDK(
            context.applicationContext as Application,
            ReccoStyle(
                font = getSelectedReccoFont(),
                palette = getSelectedReccoPalette()
            )
        )
    }

    fun getSelectedPaletteId(): Int = prefs.getInt(RECCO_PALETTE_ID_KEY, FRESH_PALETTE_ID)

    suspend fun getSelectedReccoPalette(): ReccoPalette =
        getPalettes().first().first { it.id == getSelectedPaletteId() }
            .asReccoPalette()

    fun getDefaultPalette(): ShowcasePalette = ReccoPalette.Fresh.asShowcasePalette()

    suspend fun setReccoFont(font: ReccoFont) {
        with(prefs.edit()) {
            putString(RECCO_FONT_KEY, font.fontName)
            apply()
        }

        ShowcaseApp.initSDK(
            context.applicationContext as Application,
            ReccoStyle(
                font = getSelectedReccoFont(),
                palette = getSelectedReccoPalette()
            )
        )
    }

    fun getSelectedReccoFont(): ReccoFont {
        val fontName = prefs.getString(RECCO_FONT_KEY, "")
        return if (fontName.isNullOrBlank()) {
            ReccoFont.POPPINS
        } else {
            ReccoFont.values().first { it.fontName == fontName }
        }
    }

    suspend fun getPalette(id: Int): ShowcasePalette = paletteDao.get(id).asShowcasePalette()

    fun getPalettes(): Flow<List<ShowcasePalette>> {
        return paletteDao.getAll()
            .map {
                it.map(ShowcasePaletteEntity::asShowcasePalette)
            }.map { customPalettes ->
                listOf(ReccoPalette.Fresh, ReccoPalette.Ocean, ReccoPalette.Spring, ReccoPalette.Tech)
                    .map { it.asShowcasePalette() }
                    .plus(customPalettes)
            }
    }

    suspend fun addPalette(showcasePalette: ShowcasePalette) {
        setSelectedPaletteId(
            paletteDao.add(showcasePalette.asShowcasePaletteEntity(setId = false)).toInt()
        )
    }

    suspend fun updatePalette(showcasePalette: ShowcasePalette) {
        paletteDao.update(showcasePalette.asShowcasePaletteEntity())
        setSelectedPaletteId(showcasePalette.id)
    }

    suspend fun deletePalette(showcasePalette: ShowcasePalette) {
        paletteDao.delete(showcasePalette.asShowcasePaletteEntity())
        setSelectedPaletteId(FRESH_PALETTE_ID)
    }

    companion object {
        private const val USER_ID_KEY = "user_id_key"
        private const val RECCO_PALETTE_ID_KEY = "recco_palette_id_key"
        private const val RECCO_FONT_KEY = "recco_font_key"
    }
}
