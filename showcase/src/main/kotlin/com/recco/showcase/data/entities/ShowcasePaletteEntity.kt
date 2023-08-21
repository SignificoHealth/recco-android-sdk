package com.recco.showcase.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "palette")
data class ShowcasePaletteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    val name: String,

    val primaryLight: String,
    val onPrimaryLight: String,
    val backgroundLight: String,
    val onBackgroundLight: String,
    val accentLight: String,
    val onAccentLight: String,
    val illustrationLight: String,
    val illustrationOutlineLight: String,

    val primaryDark: String,
    val onPrimaryDark: String,
    val backgroundDark: String,
    val onBackgroundDark: String,
    val accentDark: String,
    val onAccentDark: String,
    val illustrationDark: String,
    val illustrationOutlineDark: String
)
