package com.recco.api.model

sealed class ReccoPalette {
    abstract val darkColors: ReccoColors
    abstract val lightColors: ReccoColors

    object Fresh : ReccoPalette() {
        override val darkColors: ReccoColors
            get() = ReccoColors(
                primary = "#FFffe6b0",
                onPrimary = "#FF383b45",
                background = "#FF383b45",
                onBackground = "#FFffffff",
                accent = "#FF7b61ff",
                onAccent = "#FFffe5ae",
                illustration = "#FFf5b731",
                illustrationOutline = "#FF7b61ff"
            )

        override val lightColors
            get() = ReccoColors(
                primary = "#FF383b45",
                onPrimary = "#FFFFFFFF",
                background = "#FFfcfcfc",
                onBackground = "#FF383b45",
                accent = "#FF7b61ff",
                onAccent = "#FF2c2783",
                illustration = "#FFf5b731",
                illustrationOutline = "#FF454138"
            )
    }

    object Ocean : ReccoPalette() {
        override val darkColors: ReccoColors
            get() = ReccoColors(
                primary = "#FFCEEEFF",
                onPrimary = "#FF263743",
                background = "#FF263743",
                onBackground = "#FFe4f6ff",
                accent = "#FF35b9ff",
                onAccent = "#FFe4f6ff",
                illustration = "#FF35b9ff",
                illustrationOutline = "#FF88493f"
            )

        override val lightColors
            get() = ReccoColors(
                primary = "#FF125e85",
                onPrimary = "#FFffffff",
                background = "#FFecf8fe",
                onBackground = "#FF0c5175",
                accent = "#FF35b9ff",
                onAccent = "#FF17445b",
                illustration = "#FFf5a08c",
                illustrationOutline = "#FF105a81"
            )
    }

    object Spring : ReccoPalette() {
        override val darkColors: ReccoColors
            get() = ReccoColors(
                primary = "#FFffddbe",
                onPrimary = "#FF383b45",
                background = "#FF383b45",
                onBackground = "#FFffffff",
                accent = "#FF3ba17a",
                onAccent = "#FFffe5ae",
                illustration = "#FFffc188",
                illustrationOutline = "#FF926500"
            )

        override val lightColors
            get() = ReccoColors(
                primary = "#FF2c956d",
                onPrimary = "#FFffffff",
                background = "#FFfcfcfc",
                onBackground = "#FF383b45",
                accent = "#FFea8822",
                onAccent = "#FF2c2783",
                illustration = "#FFffc188",
                illustrationOutline = "#FF306d49"
            )
    }

    object Tech : ReccoPalette() {
        override val darkColors: ReccoColors
            get() = ReccoColors(
                primary = "#FFe5e4a3",
                onPrimary = "#FF373733",
                background = "#FF242422",
                onBackground = "#FFe5e4a3",
                accent = "#FFe6e452",
                onAccent = "#FFffffff",
                illustration = "#FFf5b731",
                illustrationOutline = "#FF403f15"
            )

        override val lightColors
            get() = ReccoColors(
                primary = "#FF25291d",
                onPrimary = "#FFffffff",
                background = "#FFf8f9f4",
                onBackground = "#FF383b45",
                accent = "#FFbab714",
                onAccent = "#FF6a6d65",
                illustration = "#FFf5b731",
                illustrationOutline = "#FF403f15"
            )
    }

    data class Custom(
        override val darkColors: ReccoColors,
        override val lightColors: ReccoColors
    ) : ReccoPalette()
}
