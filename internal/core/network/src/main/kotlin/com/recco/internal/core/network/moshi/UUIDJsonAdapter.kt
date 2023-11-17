package com.recco.internal.core.network.moshi

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.UUID

class UUIDJsonAdapter {

    @FromJson
    fun fromJson(value: String): UUID {
        return UUID.fromString(value)
    }

    @ToJson
    fun toJson(value: java.util.UUID): String {
        return value.toString()
    }
}
