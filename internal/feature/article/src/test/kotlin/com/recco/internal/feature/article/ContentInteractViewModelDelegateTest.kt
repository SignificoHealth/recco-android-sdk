package com.recco.internal.feature.article

import com.google.common.truth.Truth.assertThat
import com.recco.internal.core.logger.Logger
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.UserInteractionRecommendation
import com.recco.internal.core.repository.RecommendationRepository
import com.recco.internal.core.test.CoroutineTestExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutineTestExtension::class)
class ContentInteractViewModelDelegateTest {
    private lateinit var repository: RecommendationRepository
    private val logger = mock<Logger>()
    private val id = ContentId("1", "2")

    @BeforeEach
    fun setup() {
        repository = mock()
    }

    @Test
    fun `bookmarked content is updated according to the opposite logic provided`() = runTest(UnconfinedTestDispatcher()) {
        val viewModelDelegate = createViewModelDelegate(
            startWith = reccomendationAs(isBookmarked = false)
        )

        repository.stubRepositoryForSuccess(id)

        viewModelDelegate.onContentUserInteract(
            ContentUserInteract.ToggleBookmarkState(id)
        )

        advanceUntilIdle()

        assertThat(viewModelDelegate.userInteraction)
            .isEqualTo(reccomendationAs(isBookmarked = true))
    }

    private fun createViewModelDelegate(startWith: UserInteractionRecommendation): ContentInteractViewModelDelegate {
        return ContentInteractViewModelDelegate(
            repository,
            logger
        ).apply {
            userInteraction = startWith
        }
    }

    private fun RecommendationRepository.stubRepositoryForSuccess(id: ContentId) {
        stub {
            onBlocking { it.setRecommendationAsViewed(any()) } doReturn Unit
            onBlocking { it.setBookmarkRecommendation(any(), any()) } doReturn Unit
            onBlocking { it.setRecommendationRating(any(), any()) } doReturn Unit
        }
    }

    private fun reccomendationAs(
        id: ContentId = this.id,
        rating: Rating = Rating.DISLIKE,
        isBookmarked: Boolean = false,
        isBookmarkLoading: Boolean = false,
        isLikeLoading: Boolean = false,
        isDislikeLoading: Boolean = false
    ): UserInteractionRecommendation {
        return UserInteractionRecommendation(
            contentId = id,
            rating = rating,
            isBookmarked = isBookmarked,
            isBookmarkLoading = isBookmarkLoading,
            isLikeLoading = isLikeLoading,
            isDislikeLoading = isDislikeLoading
        )
    }


//    @Test
//    fun `onFailure emits exceptions while logging them if ToggleLikeState`() = runTest {
//        // When
//        repository.stubForToggleRatingFailure()
//        val events = onViewModelInteraction(3, ArticleUserInteract.ToggleLikeState)
//
//        // Then
//        events.fastForEach {
//            Truth.assertThat(events.first()).isEqualTo(expectedUiStateWithError)
//        }
//
//        verifyBlocking(logger, times(1)) {
//            e(staticThrowableForTesting, null, null)
//        }
//    }

//    private fun TestScope.onViewModelInteraction(
//        eventsToDrop: Int,
//        vararg userInteractions: ContentUserInteract
//    ) = ContentInteractViewModelDelegate(repository, logger).run {
//        onContentUserInteract(
//            viewState = viewState,
//            eventsToDrop = eventsToDrop,
//            runInteractions = { userInteractions.forEach { onUserInteract(it) } }
//        )
//    }
}
