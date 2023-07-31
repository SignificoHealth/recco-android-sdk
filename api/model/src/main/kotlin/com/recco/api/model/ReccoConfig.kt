package com.recco.api.model

data class ReccoConfig(
    val apiSecret: String,
    val palette: ReccoPalette = ReccoPalette.Fresh
)
