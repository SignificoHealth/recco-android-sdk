import com.google.common.truth.Truth.assertThat
import com.recco.internal.core.logger.Logger
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.UserInteractionRecommendation
import com.recco.internal.core.repository.RecommendationRepository
import com.recco.internal.core.test.CoroutineTestExtension
import com.recco.internal.core.test.utils.staticThrowableForTesting
import com.recco.internal.feature.rating.delegates.ContentInteractViewModelDelegate
import com.recco.internal.feature.rating.delegates.ContentUserInteract
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking

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
    fun `bookmarked content is updated according to the opposite logic provided`() = runTest {
        val viewModelDelegate = createViewModelDelegate(
            startWith = reccomendationAs(isBookmarked = true)
        )

        repository.stubRepositoryForSuccess(id)

        viewModelDelegate.onContentUserInteract(
            ContentUserInteract.ToggleBookmarkState(id)
        )

        advanceUntilIdle()

        assertThat(viewModelDelegate.userInteraction)
            .isEqualTo(reccomendationAs(isBookmarked = false))
    }

    @Test
    fun `non bookmarked content is updated according to the opposite logic provided`() = runTest {
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

    @Test
    fun `liked content turns into non rated if liked again`() = runTest {
        val startState = reccomendationAs(
            isDislikeLoading = false,
            isLikeLoading = true,
            rating = Rating.LIKE
        )

        val viewModelDelegate = createViewModelDelegate(
            startWith = startState
        )

        repository.stubRepositoryForSuccess(id)

        viewModelDelegate.onContentUserInteract(
            ContentUserInteract.ToggleLikeState(id)
        )

        assertThat(viewModelDelegate.userInteraction)
            .isEqualTo(startState)

        advanceUntilIdle()

        assertThat(viewModelDelegate.userInteraction)
            .isEqualTo(
                reccomendationAs(
                    isDislikeLoading = false,
                    isLikeLoading = false,
                    rating = Rating.NOT_RATED
                )
            )
    }

    @Test
    fun `disliked article turns into non rated if disliked again`() = runTest {
        val startState = reccomendationAs(
            isDislikeLoading = true,
            isLikeLoading = false,
            rating = Rating.DISLIKE
        )

        val viewModelDelegate = createViewModelDelegate(
            startWith = startState
        )

        repository.stubRepositoryForSuccess(id)

        viewModelDelegate.onContentUserInteract(
            ContentUserInteract.ToggleDislikeState(id)
        )

        assertThat(viewModelDelegate.userInteraction)
            .isEqualTo(startState)

        advanceUntilIdle()

        assertThat(viewModelDelegate.userInteraction)
            .isEqualTo(
                reccomendationAs(
                    isDislikeLoading = false,
                    isLikeLoading = false,
                    rating = Rating.NOT_RATED
                )
            )
    }

    @Test
    fun `liked article turns into disliked if disliked`() = runTest {
        val startState = reccomendationAs(
            isDislikeLoading = true,
            isLikeLoading = false,
            rating = Rating.LIKE
        )

        val viewModelDelegate = createViewModelDelegate(
            startWith = startState
        )

        repository.stubRepositoryForSuccess(id)

        viewModelDelegate.onContentUserInteract(
            ContentUserInteract.ToggleDislikeState(id)
        )

        assertThat(viewModelDelegate.userInteraction)
            .isEqualTo(startState)

        advanceUntilIdle()

        assertThat(viewModelDelegate.userInteraction)
            .isEqualTo(
                reccomendationAs(
                    isDislikeLoading = false,
                    isLikeLoading = false,
                    rating = Rating.DISLIKE
                )
            )
    }

    @Test
    fun `nonrated article turns into liked if liked`() = runTest {
        val startState = reccomendationAs(
            isDislikeLoading = false,
            isLikeLoading = true,
            rating = Rating.NOT_RATED
        )

        val viewModelDelegate = createViewModelDelegate(
            startWith = startState
        )

        repository.stubRepositoryForSuccess(id)

        viewModelDelegate.onContentUserInteract(
            ContentUserInteract.ToggleLikeState(id)
        )

        assertThat(viewModelDelegate.userInteraction)
            .isEqualTo(startState)

        advanceUntilIdle()

        assertThat(viewModelDelegate.userInteraction)
            .isEqualTo(
                reccomendationAs(
                    isDislikeLoading = false,
                    isLikeLoading = false,
                    rating = Rating.LIKE
                )
            )
    }

    @Test
    fun `non rated article turns into disliked if disliked`() = runTest {
        val startState = reccomendationAs(
            isDislikeLoading = true,
            isLikeLoading = false,
            rating = Rating.NOT_RATED
        )

        val viewModelDelegate = createViewModelDelegate(
            startWith = startState
        )

        repository.stubRepositoryForSuccess(id)

        viewModelDelegate.onContentUserInteract(
            ContentUserInteract.ToggleDislikeState(id)
        )

        assertThat(viewModelDelegate.userInteraction)
            .isEqualTo(startState)

        advanceUntilIdle()

        assertThat(viewModelDelegate.userInteraction)
            .isEqualTo(
                reccomendationAs(
                    isDislikeLoading = false,
                    isLikeLoading = false,
                    rating = Rating.DISLIKE
                )
            )
    }

    @Test
    fun `onFailure emits exceptions while logging them if ToggleDislikeState`() = runTest {
        val viewModelDelegate = createViewModelDelegate(
            startWith = reccomendationAs(
                isDislikeLoading = true,
                isLikeLoading = false,
                rating = Rating.NOT_RATED
            )
        )

        repository.stubForToggleRatingFailure()

        viewModelDelegate.onContentUserInteract(
            ContentUserInteract.ToggleDislikeState(id)
        )

        advanceUntilIdle()

        verifyBlocking(logger, times(1)) {
            e(staticThrowableForTesting, null, null)
        }
    }

    @Test
    fun `onFailure emits exceptions while logging them if ToggleBookmarkState`() = runTest {
        val viewModelDelegate = createViewModelDelegate(
            startWith = reccomendationAs(
                isDislikeLoading = true,
                isLikeLoading = false,
                rating = Rating.NOT_RATED
            )
        )

        repository.stubForToggleBookmarkFailure()

        viewModelDelegate.onContentUserInteract(
            ContentUserInteract.ToggleBookmarkState(id)
        )

        advanceUntilIdle()

        verifyBlocking(logger, times(1)) {
            e(staticThrowableForTesting, null, null)
        }
    }

    private fun createViewModelDelegate(startWith: UserInteractionRecommendation): ContentInteractViewModelDelegate {
        return ContentInteractViewModelDelegate(
            repository,
            logger
        ).apply {
            userInteraction = startWith
        }
    }

    internal fun RecommendationRepository.stubForToggleBookmarkFailure() {
        stub {
            onBlocking { it.setRecommendationAsViewed(any()) } doReturn Unit
            onBlocking {
                it.setBookmarkRecommendation(
                    any(),
                    any()
                )
            } doThrow staticThrowableForTesting
        }
    }

    private fun RecommendationRepository.stubRepositoryForSuccess(id: ContentId) {
        stub {
            onBlocking { it.setRecommendationAsViewed(any()) } doReturn Unit
            onBlocking { it.setBookmarkRecommendation(any(), any()) } doReturn Unit
            onBlocking { it.setRecommendationRating(any(), any()) } doReturn Unit
        }
    }

    private fun RecommendationRepository.stubForToggleRatingFailure() {
        stub {
            onBlocking { it.setRecommendationAsViewed(any()) } doReturn Unit
            onBlocking {
                it.setRecommendationRating(
                    any(),
                    any()
                )
            } doThrow staticThrowableForTesting
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
}
