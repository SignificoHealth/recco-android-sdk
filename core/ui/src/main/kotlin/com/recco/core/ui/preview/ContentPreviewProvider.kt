package com.recco.core.ui.preview

import com.recco.core.model.recommendation.ContentId
import java.util.UUID

class ContentPreviewProvider {
    companion object {
        val data
            get() = ContentId(
                itemId = UUID.randomUUID().toString(),
                catalogId = UUID.randomUUID().toString()
            )
    }
}