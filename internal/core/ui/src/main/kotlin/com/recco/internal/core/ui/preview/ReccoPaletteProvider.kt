package com.recco.internal.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.recco.api.model.ReccoPalette

class ReccoPaletteProvider : PreviewParameterProvider<ReccoPalette> {
    override val values
        get() = sequenceOf(
            ReccoPalette.Fresh, ReccoPalette.Ocean, ReccoPalette.Spring, ReccoPalette.Tech
        )
}


