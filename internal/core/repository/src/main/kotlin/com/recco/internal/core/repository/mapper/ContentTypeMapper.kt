package com.recco.internal.core.repository.mapper

import com.recco.internal.core.model.recommendation.ContentType
import com.recco.internal.core.openapi.model.ContentTypeDTO
import com.recco.internal.core.openapi.model.ContentTypeDTO.ARTICLES
import com.recco.internal.core.openapi.model.ContentTypeDTO.AUDIOS
import com.recco.internal.core.openapi.model.ContentTypeDTO.QUESTIONNAIRES
import com.recco.internal.core.openapi.model.ContentTypeDTO.VIDEOS

internal fun ContentTypeDTO.asEntity() = when (this) {
    ARTICLES -> ARTICLES
    QUESTIONNAIRES -> QUESTIONNAIRES
    AUDIOS -> AUDIOS
    VIDEOS -> VIDEOS
}
internal fun ContentType.asDTO() = when (this) {
    ContentType.ARTICLE -> ARTICLES
    ContentType.QUESTIONNAIRE -> QUESTIONNAIRES
    ContentType.AUDIO -> AUDIOS
    ContentType.VIDEO -> VIDEOS
}
