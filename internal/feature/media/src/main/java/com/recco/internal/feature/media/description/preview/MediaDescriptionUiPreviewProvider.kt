package com.recco.internal.feature.media.description.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.recco.internal.core.model.media.Audio
import com.recco.internal.core.model.media.Video
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.Status
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.core.ui.components.UserInteractionRecommendation
import com.recco.internal.feature.media.description.MediaDescriptionUi

internal class MediaDescriptionUiPreviewProvider :
    PreviewParameterProvider<UiState<MediaDescriptionUi>> {
    override val values: Sequence<UiState<MediaDescriptionUi>>
        get() = sequenceOf(
            UiState(
                isLoading = false,
                data = MediaDescriptionUi.AudioDescriptionUi(
                    audio = AUDIO,
                    userInteraction = USER_INTERACTION
                )
            ),
            UiState(
                isLoading = false,
                data = MediaDescriptionUi.VideoDescriptionUi(
                    video = VIDEO,
                    userInteraction = USER_INTERACTION
                )
            ),
        )

    companion object {
        private val AUDIO = Audio(
            id = ContentId(
                itemId = "corrumpit",
                catalogId = "enim"
            ),
            rating = Rating.LIKE,
            status = Status.NO_INTERACTION,
            isBookmarked = false,
            audioUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            headline = "vidisse",
            imageUrl = null,
            description = "This exercise is not for you if you have a heart condition. Consult with your doctor before engaging in heavy cardio exercise",
            imageAlt = null,
            lengthInSeconds = 10
        )

        private val VIDEO = Video(
            id = ContentId(
                itemId = "corrumpit",
                catalogId = "enim"
            ),
            rating = Rating.LIKE,
            status = Status.NO_INTERACTION,
            isBookmarked = false,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            headline = "vidisse",
            imageUrl = null,
            description = "This exercise is not for you if you have a heart condition. Consult with your doctor before engaging in heavy cardio exercise",
            imageAlt = null,
            length = 10
        )

        private val USER_INTERACTION = UserInteractionRecommendation(
            rating = Rating.DISLIKE,
            isBookmarked = false,
            isBookmarkLoading = false,
            isLikeLoading = false,
            isDislikeLoading = false
        )
    }
}
