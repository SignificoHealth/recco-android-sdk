package com.recco.api.model

import androidx.compose.ui.graphics.Color

sealed class ReccoPalette {
    abstract val darkColors: ReccoColors
    abstract val lightColors: ReccoColors

    object Fresh : ReccoPalette() {
        override val darkColors: ReccoColors
            get() = ReccoColors(
                primary = Color(0xFFFFE6B0),
                onPrimary = Color(0xFF383b45),
                background = Color(0xFF383b45),
                onBackground = Color(0xFFffffff),
                accent = Color(0xFF7b61ff),
                onAccent = Color(0xFFFFE5AE),
                illustration = Color(0xFFf5b731)
            )

        override val lightColors
            get() = ReccoColors(
                primary = Color(0xFF383B45),
                onPrimary = Color(0xFFFFFFFF),
                background = Color(0xFFfcfcfc),
                onBackground = Color(0xFF383b45),
                accent = Color(0xFF7b61ff),
                onAccent = Color(0xFF2C2783),
                illustration = Color(0xFFf5b731),
            )
    }

    object Ocean : ReccoPalette() {
        override val darkColors: ReccoColors
            get() = ReccoColors(
                primary = Color(0xFFCEEEFF),
                onPrimary = Color(0xFF263743),
                background = Color(0xFF263743),
                onBackground = Color(0xFFE4F6FF),
                accent = Color(0xFF35B9FF),
                onAccent = Color(0xFFE4F6FF),
                illustration = Color(0xFF35b9ff)
            )

        override val lightColors
            get() = ReccoColors(
                primary = Color(0xFF125E85),
                onPrimary = Color(0xFFffffff),
                background = Color(0xFFECF8FE),
                onBackground = Color(0xFF0C5175),
                accent = Color(0xFF35B9FF),
                onAccent = Color(0xFF17445B),
                illustration = Color(0xFFf5a08c)
            )
    }

    data class Custom(
        override val darkColors: ReccoColors,
        override val lightColors: ReccoColors
    ) : ReccoPalette()
}