package com.recco.api.model

data class SDKConfig(
    val appName: String,
    val apiSecret: String,
    val palette: Palette = Palette.DEFAULT
)