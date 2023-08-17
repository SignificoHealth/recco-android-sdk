package com.recco.showcase.main

import com.recco.api.model.ReccoFont
import com.recco.api.model.ReccoPalette

data class MainUI(
    val selectedFont: ReccoFont? = null,
    val selectedPalette: ReccoPalette? = null
)
