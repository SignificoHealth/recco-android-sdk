package com.recco.internal.feature.bookmark.utils

import com.recco.internal.core.repository.RecommendationRepository
import com.recco.internal.feature.bookmark.model.recommendation
import com.recco.internal.feature.bookmark.model.staticThrowableForTesting
import kotlinx.coroutines.flow.flow
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub

internal fun RecommendationRepository.stubRepositoryForSuccess() {
    this.stub {
        onBlocking { it.bookmarks } doReturn flow { emit(listOf(recommendation)) }
        onBlocking { it.reloadBookmarks() } doReturn Unit
    }
}

internal fun RecommendationRepository.stubRepositoryForError() {
    this.stub {
        onBlocking { it.bookmarks } doReturn flow { throw staticThrowableForTesting }
    }
}
