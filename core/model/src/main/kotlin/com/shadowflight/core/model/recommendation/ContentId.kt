package com.shadowflight.core.model.recommendation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class ContentId(
    val itemId: String,
    val catalogId: String
) : Parcelable
