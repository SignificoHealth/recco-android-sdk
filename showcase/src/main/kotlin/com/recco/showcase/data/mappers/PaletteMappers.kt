package com.recco.showcase.data.mappers

import com.recco.api.model.ReccoColors
import com.recco.api.model.ReccoPalette
import com.recco.showcase.data.entities.ShowcasePaletteEntity
import com.recco.showcase.models.ShowcaseColors
import com.recco.showcase.models.ShowcasePalette

fun ReccoPalette.asShowcasePalette() = ShowcasePalette(
    id = when (this) {
        ReccoPalette.Fresh -> FRESH_PALETTE_ID
        ReccoPalette.Ocean -> OCEAN_PALETTE_ID
        ReccoPalette.Spring -> SPRING_PALETTE_ID
        ReccoPalette.Tech -> TECH_PALETTE_ID
        is ReccoPalette.Custom -> throw IllegalArgumentException("$this is not a supported palette")
    },
    name = when (this) {
        ReccoPalette.Fresh -> FRESH_PALETTE_NAME
        ReccoPalette.Ocean -> OCEAN_PALETTE_NAME
        ReccoPalette.Spring -> SPRING_PALETTE_NAME
        ReccoPalette.Tech -> TECH_PALETTE_NAME
        is ReccoPalette.Custom -> throw IllegalArgumentException("$this is not a supported palette")
    },
    darkColors = darkColors.asShowcaseColors(),
    lightColors = lightColors.asShowcaseColors(),
    isCustom = false
)

private fun ReccoColors.asShowcaseColors() = ShowcaseColors(
    primary,
    onPrimary,
    background,
    onBackground,
    accent,
    onAccent,
    illustration,
    illustrationOutline
)

fun ShowcasePalette.asReccoPalette(): ReccoPalette = when (id) {
    FRESH_PALETTE_ID -> ReccoPalette.Fresh
    OCEAN_PALETTE_ID -> ReccoPalette.Ocean
    SPRING_PALETTE_ID -> ReccoPalette.Spring
    TECH_PALETTE_ID -> ReccoPalette.Tech
    else -> ReccoPalette.Custom(
        lightColors = lightColors.asReccoColors(),
        darkColors = darkColors.asReccoColors()
    )
}

private fun ShowcaseColors.asReccoColors() = ReccoColors(
    primary,
    onPrimary,
    background,
    onBackground,
    accent,
    onAccent,
    illustration,
    illustrationOutline
)

fun ShowcasePalette.asShowcasePaletteEntity(setId: Boolean = true) = ShowcasePaletteEntity(
    name = name,
    primaryLight = lightColors.primary,
    onPrimaryLight = lightColors.onPrimary,
    backgroundLight = lightColors.background,
    onBackgroundLight = lightColors.onBackground,
    accentLight = lightColors.accent,
    onAccentLight = lightColors.onAccent,
    illustrationLight = lightColors.illustration,
    illustrationOutlineLight = lightColors.illustrationOutline,
    primaryDark = darkColors.primary,
    onPrimaryDark = darkColors.onPrimary,
    backgroundDark = darkColors.background,
    onBackgroundDark = darkColors.onBackground,
    accentDark = darkColors.accent,
    onAccentDark = darkColors.onAccent,
    illustrationDark = darkColors.illustration,
    illustrationOutlineDark = darkColors.illustrationOutline
).let { palette ->
    if (setId) {
        palette.copy(id = id)
    } else {
        palette
    }
}

fun ShowcasePaletteEntity.asShowcasePalette() = ShowcasePalette(
    id = id,
    name = name,
    darkColors = ShowcaseColors(
        primary = primaryDark,
        onPrimary = onPrimaryDark,
        background = backgroundDark,
        onBackground = onBackgroundDark,
        accent = accentDark,
        onAccent = onAccentDark,
        illustration = illustrationDark,
        illustrationOutline = illustrationOutlineDark
    ),
    lightColors = ShowcaseColors(
        primary = primaryLight,
        onPrimary = onPrimaryLight,
        background = backgroundLight,
        onBackground = onBackgroundLight,
        accent = accentLight,
        onAccent = onAccentLight,
        illustration = illustrationLight,
        illustrationOutline = illustrationOutlineLight
    ),
    isCustom = true
)

// The faked id values adopt negative values to avoid clashing with Room auto-generated ids
const val FRESH_PALETTE_ID = -1
private const val OCEAN_PALETTE_ID = -2
private const val SPRING_PALETTE_ID = -3
private const val TECH_PALETTE_ID = -4

private const val FRESH_PALETTE_NAME = "Fresh"
private const val OCEAN_PALETTE_NAME = "Ocean"
private const val SPRING_PALETTE_NAME = "Spring"
private const val TECH_PALETTE_NAME = "Tech"
