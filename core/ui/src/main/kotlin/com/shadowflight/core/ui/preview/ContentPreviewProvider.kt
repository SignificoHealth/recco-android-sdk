package com.shadowflight.core.ui.preview

import com.shadowflight.core.model.recommendation.ContentId
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