package com.recco.internal.feature.media.description.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.recco.internal.core.model.media.Audio
import com.recco.internal.core.model.media.Video
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.Rating
import com.recco.internal.core.model.recommendation.Status
import com.recco.internal.core.model.recommendation.UserInteractionRecommendation
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.feature.media.description.MediaDescriptionUI

internal class MediaDescriptionUiPreviewProvider :
    PreviewParameterProvider<UiState<MediaDescriptionUI>> {
    override val values: Sequence<UiState<MediaDescriptionUI>>
        get() = sequenceOf(
            UiState(
                isLoading = false,
                data = MediaDescriptionUI.AudioDescriptionUI(audio = AUDIO)
            ),
            UiState(
                isLoading = false,
                data = MediaDescriptionUI.VideoDescriptionUI(video = VIDEO)
            ),
            UiState(
                isLoading = false,
                data = MediaDescriptionUI.AudioDescriptionUI(audio = AUDIO_WITHOUT_TRANSCRIPTION)
            )
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
            headline = "Sleeping like a baby",
            imageUrl = null,
            description = "A et est. Non veniam voluptates harum. " +
                "Sint explicabo sit eaque reprehenderit qui. Minus repudiandae qui et." +
                " Veniam neque repudiandae non. Maiores est dolor maxime sit fugiat. " +
                "Quas in sed maxime veniam placeat provident nemo laborum. " +
                "Illum natus laboriosam autem voluptatem neque labore voluptate sapiente aut. " +
                "Ratione architecto dolor. Expedita est voluptas nesciunt." +
                "Exercitationem placeat possimus aliquam et pariatur nemo assumenda sit. " +
                "Esse quod voluptatum et rerum a voluptatum odio. Dicta cumque aut excepturi.",
            imageAlt = null,
            hasTranscription = true,
            lengthInSeconds = 7200
        )

        private val AUDIO_WITHOUT_TRANSCRIPTION = Audio(
            id = ContentId(
                itemId = "corrumpit",
                catalogId = "enim"
            ),
            rating = Rating.LIKE,
            status = Status.NO_INTERACTION,
            isBookmarked = false,
            audioUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            headline = "Sleeping like a baby",
            imageUrl = null,
            description = null,
            hasTranscription = false,
            imageAlt = null,
            lengthInSeconds = 7200
        )

        private val VIDEO = Video(
            id = ContentId(
                itemId = "corrumpit",
                catalogId = "enim"
            ),
            rating = Rating.LIKE,
            status = Status.NO_INTERACTION,
            isBookmarked = false,
            videoUrl = "https://google.es",
            headline = "10 min full mobility stretching",
            imageUrl = null,
            description = "Exercitationem placeat possimus aliquam et pariatur nemo" +
                " assumenda sit. Esse quod voluptatum et rerum a voluptatum odio. " +
                "Dicta cumque aut excepturi. Ut a tempora voluptatum qui consequuntur.",
            disclaimer = "This exercise is not for you if you have a heart condition. " +
                "Consult with your doctor before engaging in heavy cardio exercise",
            imageAlt = null,
            lengthInSeconds = 600
        )

        private val USER_INTERACTION = UserInteractionRecommendation(
            contentId = ContentId("1", "2"),
            rating = Rating.DISLIKE,
            isBookmarked = false,
            isBookmarkLoading = false,
            isLikeLoading = false,
            isDislikeLoading = false
        )
    }
}
