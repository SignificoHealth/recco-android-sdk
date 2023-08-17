package com.recco.api.model

/*
Color as string. Supported formats are:
    #RRGGBB
    #AARRGGBB
 */
data class ReccoColors(
    val primary: String,
    val onPrimary: String,
    val background: String,
    val onBackground: String,
    val accent: String,
    val onAccent: String,
    val illustration: String,
    val illustrationOutline: String
)
