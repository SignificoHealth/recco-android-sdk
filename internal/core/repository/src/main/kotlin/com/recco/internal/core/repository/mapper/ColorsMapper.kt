package com.recco.internal.core.repository.mapper

import com.recco.api.model.ReccoColors
import com.recco.internal.core.openapi.model.ColorsDTO

internal fun ColorsDTO.asEntity(): ReccoColors {
    return ReccoColors(
        primary = primary.rearrangeHexColorAlphaToStart(),
        onPrimary = onPrimary.rearrangeHexColorAlphaToStart(),
        background = background.rearrangeHexColorAlphaToStart(),
        onBackground = onBackground.rearrangeHexColorAlphaToStart(),
        accent = accent.rearrangeHexColorAlphaToStart(),
        onAccent = onAccent.rearrangeHexColorAlphaToStart(),
        illustration = illustration.rearrangeHexColorAlphaToStart(),
        illustrationOutline = illustrationOutline.rearrangeHexColorAlphaToStart()
    )
}

/**
 * Rearranges the alpha component of a hexadecimal color string from #RRGGBBAA format to #AARRGGBB format.
 *
 * This function is useful for accommodating differences in how iOS and Android manage color values.
 *
 * @return A new string with the alpha component moved to the beginning of the string.
 * If the input format is not #RRGGBBAA, throws an error
 */
fun String.rearrangeHexColorAlphaToStart(): String {
    if (length != 9 || !startsWith("#")) {
        // Invalid input format
        error("Hex color does not have a (#RRGGBBAA) valid format: $this")
    }

    val alpha = substring(7)
    val color = substring(1, 7)

    return "#$alpha$color"
}
