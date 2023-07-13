package com.recco.api.model

import androidx.compose.ui.graphics.Color

sealed class ReccoPalette {
    abstract val darkColors: ReccoColors
    abstract val lightColors: ReccoColors

    object Default : ReccoPalette() {
        override val darkColors: ReccoColors
            get() = defaultPaletteColors
        override val lightColors: ReccoColors
            get() = defaultPaletteColors
    }

    data class Custom(
        override val darkColors: ReccoColors,
        override val lightColors: ReccoColors
    ) : ReccoPalette()
}

private val defaultPaletteColors = ReccoColors(
    primary = Color(0xFF383B45),
    onPrimary = Color(0xFFFFFFFF),
    background = Color(0xFFFCFCFC),
    onBackground = Color(0xFF383B45),
    accent = Color(0xFF7B61FF),
    onAccent = Color(0xFF2C2783),
    illustration = Color(0xFFF5B731),
    surface = Color(0xFFDAD7D7),
    onSurface = Color(0xFF383B45)
)