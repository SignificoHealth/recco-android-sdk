package com.recco.showcase.customize

import com.recco.showcase.models.ShowcasePalette

data class CustomizePaletteUI(
    val palette: ShowcasePalette,
    val isDarkMode: Boolean = false,
    val isEditing: Boolean = false
)
