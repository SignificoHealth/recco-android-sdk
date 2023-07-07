package com.recco.internal.core.ui.preview

import com.recco.internal.core.model.recommendation.ContentId
import java.util.UUID

class ContentIdPreviewProvider {
    companion object {
        val data
            get() = ContentId(
                itemId = UUID.randomUUID().toString(),
                catalogId = UUID.randomUUID().toString()
            )
    }
}