package com.recco.internal.core.repository.mapper

import com.recco.api.model.ReccoColors
import com.recco.internal.core.openapi.model.ColorsDTO

internal fun ColorsDTO.asEntity() = ReccoColors(
    primary = primary,
    onPrimary = onPrimary,
    background = background,
    onBackground = onBackground,
    accent = accent,
    onAccent = onAccent,
    illustration = illustration,
    illustrationOutline = illustrationOutline
)
