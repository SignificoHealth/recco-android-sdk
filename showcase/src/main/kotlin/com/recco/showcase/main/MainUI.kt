package com.recco.showcase.main

import com.recco.api.model.ReccoFont
import com.recco.showcase.data.mappers.FRESH_PALETTE_ID
import com.recco.showcase.models.ShowcasePalette

data class MainUI(
    val palettes: List<ShowcasePalette> = emptyList(),
    val selectedFont: ReccoFont? = null,
    val selectedPaletteId: Int = FRESH_PALETTE_ID,
)
