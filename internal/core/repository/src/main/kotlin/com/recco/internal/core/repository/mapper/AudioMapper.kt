package com.recco.internal.core.repository.mapper

import com.recco.internal.core.model.media.Audio
import com.recco.internal.core.openapi.model.AppUserAudioDTO

fun AppUserAudioDTO.asEntity(): Audio {
    return Audio(
        id = id.asEntity(),
        rating = rating.asEntity(),
        status = status.asEntity(),
        isBookmarked = bookmarked,
        audioUrl = audioUrl,
        headline = headline,
        imageUrl = dynamicImageResizingUrl,
        imageAlt = imageAlt,
        length = length
    )
}
