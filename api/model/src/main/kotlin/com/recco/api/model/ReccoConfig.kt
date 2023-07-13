package com.recco.api.model

data class ReccoConfig(
    val appName: String,
    val apiSecret: String,
    val palette: ReccoPalette = ReccoPalette.Default
)