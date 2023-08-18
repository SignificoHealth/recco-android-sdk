package com.recco.showcase.models

data class ShowcasePalette(
    val id: Int,
    val name: String,
    val darkColors: ShowcaseColors,
    val lightColors: ShowcaseColors
)
