package com.recco.internal.core.repository.mapper

import com.recco.internal.core.model.media.Video
import com.recco.internal.core.openapi.model.AppUserVideoDTO

fun AppUserVideoDTO.asEntity(): Video {
    return Video(
        id = id.asEntity(),
        rating = rating.asEntity(),
        status = status.asEntity(),
        isBookmarked = bookmarked,
        videoUrl = videoUrl,
        headline = headline,
        imageUrl = dynamicImageResizingUrl,
        imageAlt = imageAlt,
        length = length,
        description = description,
        disclaimer = disclaimer
    )
}
