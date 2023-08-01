package com.recco.api.model

data class ReccoConfig(
    val clientSecret: String,
    val palette: ReccoPalette = ReccoPalette.Fresh
)
