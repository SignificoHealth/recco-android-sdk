package com.recco.internal.feature.bookmark

import com.recco.internal.core.repository.FeedRepository
import com.recco.internal.core.repository.MetricRepository
import com.recco.internal.core.repository.RecommendationRepository
import com.recco.internal.core.test.utils.staticThrowableForTesting
import kotlinx.coroutines.flow.flow
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.stub

internal fun FeedRepository.stubRepositoryForSuccess() {
    stub {
        onBlocking { it.feedSections } doReturn flow { emit(feedUI.sections.map { it.feedSection }) }
        onBlocking { it.reloadFeed() } doReturn Unit
    }
}

internal fun RecommendationRepository.stubRepositoryForSuccess() {
    stub {
        onBlocking { it.tailoredPhysicalActivity } doReturn flow {
            emit(feedUI.sections.first().recommendations)
        }
        onBlocking { it.explorePhysicalActivity } doReturn flow {
            emit(feedUI.sections.first().recommendations)
        }
        onBlocking { it.tailoredNutrition } doReturn flow {
            emit(feedUI.sections.first().recommendations)
        }
        onBlocking { it.exploreNutrition } doReturn flow {
            emit(feedUI.sections.first().recommendations)
        }
        onBlocking { it.tailoredPhysicalWellbeing } doReturn flow {
            emit(feedUI.sections.first().recommendations)
        }
        onBlocking { it.explorePhysicalWellbeing } doReturn flow {
            emit(feedUI.sections.first().recommendations)
        }
        onBlocking { it.tailoredSleep } doReturn flow {
            emit(feedUI.sections.first().recommendations)
        }
        onBlocking { it.exploreSleep } doReturn flow {
            emit(feedUI.sections.first().recommendations)
        }
        onBlocking { it.preferredRecommendations } doReturn flow {
            emit(feedUI.sections.first().recommendations)
        }
        onBlocking { it.mostPopular } doReturn flow {
            emit(feedUI.sections.first().recommendations)
        }
        onBlocking { it.newestContent } doReturn flow {
            emit(feedUI.sections.first().recommendations)
        }
        onBlocking { it.starting } doReturn flow {
            emit(feedUI.sections.first().recommendations)
        }
        onBlocking { it.reloadAllSections() } doReturn Unit
    }
}

internal fun FeedRepository.stubRepositoryForError() {
    stub {
        onBlocking { it.feedSections } doReturn flow { throw staticThrowableForTesting }
        onBlocking { it.reloadFeed() } doThrow staticThrowableForTesting
    }
}

internal fun RecommendationRepository.stubRepositoryForError() {
    stub {
        onBlocking { it.tailoredPhysicalActivity } doReturn flow { throw staticThrowableForTesting }
        onBlocking { it.explorePhysicalActivity } doReturn flow { throw staticThrowableForTesting }
        onBlocking { it.tailoredNutrition } doReturn flow { throw staticThrowableForTesting }
        onBlocking { it.exploreNutrition } doReturn flow { throw staticThrowableForTesting }
        onBlocking { it.tailoredPhysicalWellbeing } doReturn flow { throw staticThrowableForTesting }
        onBlocking { it.explorePhysicalWellbeing } doReturn flow { throw staticThrowableForTesting }
        onBlocking { it.tailoredSleep } doReturn flow { throw staticThrowableForTesting }
        onBlocking { it.exploreSleep } doReturn flow { throw staticThrowableForTesting }
        onBlocking { it.preferredRecommendations } doReturn flow { throw staticThrowableForTesting }
        onBlocking { it.mostPopular } doReturn flow { throw staticThrowableForTesting }
        onBlocking { it.newestContent } doReturn flow { throw staticThrowableForTesting }
        onBlocking { it.starting } doReturn flow { throw staticThrowableForTesting }
    }
}

internal fun MetricRepository.stubRepositoryForError() {
    stub {
        onBlocking { it.logEvent(any()) } doThrow staticThrowableForTesting
    }
}
