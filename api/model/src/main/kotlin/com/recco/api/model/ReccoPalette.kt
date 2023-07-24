package com.recco.api.model

import androidx.compose.ui.graphics.Color

sealed class ReccoPalette {
    abstract val darkColors: ReccoColors
    abstract val lightColors: ReccoColors

    object Fresh : ReccoPalette() {
        override val darkColors: ReccoColors
            get() = ReccoColors(
                primary = Color(0xFFffe6b0),
                onPrimary = Color(0xFF383b45),
                background = Color(0xFF383b45),
                onBackground = Color(0xFFffffff),
                accent = Color(0xFF7b61ff),
                onAccent = Color(0xFFffe5ae),
                illustration = Color(0xFFf5b731)
            )

        override val lightColors
            get() = ReccoColors(
                primary = Color(0xFF383b45),
                onPrimary = Color(0xFFFFFFFF),
                background = Color(0xFFfcfcfc),
                onBackground = Color(0xFF383b45),
                accent = Color(0xFF7b61ff),
                onAccent = Color(0xFF2c2783),
                illustration = Color(0xFFf5b731)
            )
    }

    object Ocean : ReccoPalette() {
        override val darkColors: ReccoColors
            get() = ReccoColors(
                primary = Color(0xFFCEEEFF),
                onPrimary = Color(0xFF263743),
                background = Color(0xFF263743),
                onBackground = Color(0xFFe4f6ff),
                accent = Color(0xFF35b9ff),
                onAccent = Color(0xFFe4f6ff),
                illustration = Color(0xFF35b9ff)
            )

        override val lightColors
            get() = ReccoColors(
                primary = Color(0xFF125e85),
                onPrimary = Color(0xFFffffff),
                background = Color(0xFFecf8fe),
                onBackground = Color(0xFF0c5175),
                accent = Color(0xFF35b9ff),
                onAccent = Color(0xFF17445b),
                illustration = Color(0xFFf5a08c)
            )
    }

    data class Custom(
        override val darkColors: ReccoColors,
        override val lightColors: ReccoColors
    ) : ReccoPalette()
}
