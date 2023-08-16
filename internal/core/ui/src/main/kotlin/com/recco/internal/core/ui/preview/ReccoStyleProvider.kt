package com.recco.internal.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.recco.api.model.ReccoFont
import com.recco.api.model.ReccoPalette
import com.recco.api.model.ReccoStyle

class ReccoStyleProvider : PreviewParameterProvider<ReccoStyle> {
    override val values
        get() = sequenceOf(
            ReccoStyle(ReccoFont.POPPINS, ReccoPalette.Fresh),
            ReccoStyle(ReccoFont.ROBOTO, ReccoPalette.Ocean),
            ReccoStyle(ReccoFont.MONTSERRAT, ReccoPalette.Spring),
            ReccoStyle(ReccoFont.WORK_SANS, ReccoPalette.Tech)
        )
}
