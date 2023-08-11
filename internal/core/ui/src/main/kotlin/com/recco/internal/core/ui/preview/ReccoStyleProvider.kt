package com.recco.internal.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.recco.api.model.ReccoStyle

class ReccoStyleProvider : PreviewParameterProvider<ReccoStyle> {
    override val values
        get() = sequenceOf(
            ReccoStyle.Fresh,
            ReccoStyle.Ocean,
            ReccoStyle.Spring,
            ReccoStyle.Tech
        )
}
