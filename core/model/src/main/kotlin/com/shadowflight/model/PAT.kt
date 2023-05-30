package com.shadowflight.model

import java.time.OffsetDateTime

data class PAT(
    val accessToken: String,
    val expirationDate: OffsetDateTime
)

fun PAT.didTokenExpired() = expirationDate.isBefore(OffsetDateTime.now())