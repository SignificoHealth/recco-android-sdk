package com.recco.internal.core.media.mapper

import com.recco.internal.core.model.media.MediaType
import com.recco.internal.core.model.recommendation.ContentType
import com.recco.internal.core.model.recommendation.ContentType.AUDIO
import com.recco.internal.core.model.recommendation.ContentType.VIDEO

fun ContentType.asMediaType(): MediaType {
    return when (this) {
        AUDIO -> MediaType.AUDIO
        VIDEO -> MediaType.VIDEO
        else -> error("$this cannot be parsed as MediaType")
    }
}
