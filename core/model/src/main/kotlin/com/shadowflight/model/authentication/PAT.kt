package com.shadowflight.model.authentication

import java.time.OffsetDateTime

data class PAT(
    val accessToken: String,
    val tokenId: String,
    val expirationDate: OffsetDateTime
)

fun PAT.isTokenExpired() = expirationDate.isBefore(OffsetDateTime.now())