package com.recco.internal.core.ui.navigation

import android.net.Uri
import android.os.Bundle
import androidx.core.os.BundleCompat
import androidx.navigation.NavType
import com.recco.internal.core.model.recommendation.ContentId
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val ContentIdNavType = object : NavType<ContentId>(isNullableAllowed = true) {
    override fun put(bundle: Bundle, key: String, value: ContentId) {
        bundle.putParcelable(key, value)
    }

    override fun get(bundle: Bundle, key: String): ContentId? {
        return BundleCompat.getParcelable(bundle, key, ContentId::class.java)
    }

    override fun parseValue(value: String): ContentId = Json.decodeFromString(value)

    override fun serializeAsValue(value: ContentId) = Uri.encode(Json.encodeToString(value))
}