package com.recco.api.model

data class ReccoConfig(
    val clientSecret: String,
    val style: ReccoStyle = ReccoStyle.Fresh
)
