package com.shadowflight.uicommons.preview

import com.shadowflight.model.recommendation.ContentId
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