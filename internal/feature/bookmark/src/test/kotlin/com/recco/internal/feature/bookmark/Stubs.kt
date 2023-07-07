package com.recco.internal.feature.bookmark

import com.recco.internal.core.repository.RecommendationRepository
import com.recco.internal.core.test.utils.staticThrowableForTesting
import kotlinx.coroutines.flow.flow
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub

internal fun RecommendationRepository.stubRepositoryForSuccess() {
    stub {
        onBlocking { it.bookmarks } doReturn flow { emit(bookmarkUI.recommendations) }
        onBlocking { it.reloadBookmarks() } doReturn Unit
    }
}

internal fun RecommendationRepository.stubRepositoryForError() {
    stub {
        onBlocking { it.bookmarks } doReturn flow { throw staticThrowableForTesting }
    }
}
