package com.recco.api.model

import androidx.compose.ui.graphics.Color

sealed class ReccoStyle {
    abstract val darkColors: ReccoColors
    abstract val lightColors: ReccoColors

    object Fresh : ReccoStyle() {
        override val darkColors: ReccoColors
            get() = ReccoColors(
                primary = Color(0xFFffe6b0),
                onPrimary = Color(0xFF383b45),
                background = Color(0xFF383b45),
                onBackground = Color(0xFFffffff),
                accent = Color(0xFF7b61ff),
                onAccent = Color(0xFFffe5ae),
                illustration = Color(0xFFf5b731),
                illustrationOutline = Color(0xFF7b61ff)
            )

        override val lightColors
            get() = ReccoColors(
                primary = Color(0xFF383b45),
                onPrimary = Color(0xFFFFFFFF),
                background = Color(0xFFfcfcfc),
                onBackground = Color(0xFF383b45),
                accent = Color(0xFF7b61ff),
                onAccent = Color(0xFF2c2783),
                illustration = Color(0xFFf5b731),
                illustrationOutline = Color(0xFF454138)
            )
    }

    object Ocean : ReccoStyle() {
        override val darkColors: ReccoColors
            get() = ReccoColors(
                primary = Color(0xFFCEEEFF),
                onPrimary = Color(0xFF263743),
                background = Color(0xFF263743),
                onBackground = Color(0xFFe4f6ff),
                accent = Color(0xFF35b9ff),
                onAccent = Color(0xFFe4f6ff),
                illustration = Color(0xFF35b9ff),
                illustrationOutline = Color(0xFF88493f)
            )

        override val lightColors
            get() = ReccoColors(
                primary = Color(0xFF125e85),
                onPrimary = Color(0xFFffffff),
                background = Color(0xFFecf8fe),
                onBackground = Color(0xFF0c5175),
                accent = Color(0xFF35b9ff),
                onAccent = Color(0xFF17445b),
                illustration = Color(0xFFf5a08c),
                illustrationOutline = Color(0xFF105a81)
            )
    }

    object Spring : ReccoStyle() {
        override val darkColors: ReccoColors
            get() = ReccoColors(
                primary = Color(0xFFffddbe),
                onPrimary = Color(0xFF383b45),
                background = Color(0xFF383b45),
                onBackground = Color(0xFFffffff),
                accent = Color(0xFF3ba17a),
                onAccent = Color(0xFFffe5ae),
                illustration = Color(0xFFffc188),
                illustrationOutline = Color(0xFF926500)
            )

        override val lightColors
            get() = ReccoColors(
                primary = Color(0xFF2c956d),
                onPrimary = Color(0xFFffffff),
                background = Color(0xFFfcfcfc),
                onBackground = Color(0xFF383b45),
                accent = Color(0xFFea8822),
                onAccent = Color(0xFF2c2783),
                illustration = Color(0xFFffc188),
                illustrationOutline = Color(0xFF306d49)
            )
    }

    object Tech : ReccoStyle() {
        override val darkColors: ReccoColors
            get() = ReccoColors(
                primary = Color(0xFFe5e4a3),
                onPrimary = Color(0xFF373733),
                background = Color(0xFF242422),
                onBackground = Color(0xFFe5e4a3),
                accent = Color(0xFFe6e452),
                onAccent = Color(0xFFffffff),
                illustration = Color(0xFFf5b731),
                illustrationOutline = Color(0xFF403f15)
            )

        override val lightColors
            get() = ReccoColors(
                primary = Color(0xFF25291d),
                onPrimary = Color(0xFFffffff),
                background = Color(0xFFf8f9f4),
                onBackground = Color(0xFF383b45),
                accent = Color(0xFFbab714),
                onAccent = Color(0xFF6a6d65),
                illustration = Color(0xFFf5b731),
                illustrationOutline = Color(0xFF403f15)
            )
    }

    data class Custom(
        override val darkColors: ReccoColors,
        override val lightColors: ReccoColors
    ) : ReccoStyle()
}
