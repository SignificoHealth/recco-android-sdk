package com.recco.api.model

data class ReccoConfig(
    val appName: String,
    val clientSecret: String,
    val palette: ReccoPalette = ReccoPalette.Fresh
)
