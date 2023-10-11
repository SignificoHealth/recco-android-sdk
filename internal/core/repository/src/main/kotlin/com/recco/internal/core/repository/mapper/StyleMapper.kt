package com.recco.internal.core.repository.mapper

import com.recco.api.model.ReccoFont
import com.recco.api.model.ReccoPalette
import com.recco.api.model.ReccoStyle
import com.recco.internal.core.openapi.model.StyleDTO

internal fun StyleDTO.asEntity() = ReccoStyle(
    font = ReccoFont.valueOf(androidFont.value.uppercase()),
    palette = ReccoPalette.Custom(
        darkColors = darkColors.asEntity(),
        lightColors = lightColors.asEntity(),
    )
)
