package com.recco.internal.feature.media.description

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recco.internal.core.logger.Logger
import com.recco.internal.core.model.media.Audio
import com.recco.internal.core.model.media.Video
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.ContentType
import com.recco.internal.core.model.recommendation.ContentType.AUDIO
import com.recco.internal.core.model.recommendation.ContentType.VIDEO
import com.recco.internal.core.repository.RecommendationRepository
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.core.ui.components.UserInteractionRecommendation
import com.recco.internal.core.ui.components.toUiState
import com.recco.internal.feature.media.description.navigation.contentTypeArg
import com.recco.internal.feature.media.description.navigation.idArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MediaDescriptionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val reccomendationRepository: RecommendationRepository,
    private val logger: Logger
) : ViewModel() {
    private val _viewState = MutableStateFlow(UiState<MediaDescriptionUi>())
    val contentId by lazy { checkNotNull(savedStateHandle.get<ContentId>(idArg)) }
    val contentType by lazy { checkNotNull(savedStateHandle.get<ContentType>(contentTypeArg)) }
    val viewState: Flow<UiState<MediaDescriptionUi>> = _viewState

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            runCatching { loadMedia() }
                .onSuccess { _viewState.value = it }
                .onFailure {
                    _viewState.value = it.toUiState()
                    logger.e(it)
                }
        }
    }

    private suspend fun loadMedia(): UiState<MediaDescriptionUi> {
        return when (contentType) {
            AUDIO -> reccomendationRepository.getAudio(contentId).toUiState()
            VIDEO -> reccomendationRepository.getVideo(contentId).toUiState()
            else -> error("Attempted to load $contentType in MediaDescriptionViewModel")
        }
    }

    private fun Audio.toUiState(): UiState<MediaDescriptionUi> {
        return UiState(
            isLoading = false,
            error = null,
            data = MediaDescriptionUi.AudioDescriptionUi(
                audio = this,
                userInteraction = UserInteractionRecommendation(
                    rating = this.rating,
                    isBookmarked = this.isBookmarked
                )
            )
        )
    }

    private fun Video.toUiState(): UiState<MediaDescriptionUi> {
        return UiState(
            isLoading = false,
            error = null,
            data = MediaDescriptionUi.VideoDescriptionUi(
                video = this,
                userInteraction = UserInteractionRecommendation(
                    rating = this.rating,
                    isBookmarked = this.isBookmarked
                )
            )
        )
    }

    fun onUserInteract(mediaDescriptionUserInteract: MediaDescriptionUserInteract) {
        when (mediaDescriptionUserInteract) {
            MediaDescriptionUserInteract.OnPlayMedia -> TODO()
            MediaDescriptionUserInteract.Retry -> {
                loadData()
            }
            MediaDescriptionUserInteract.ToggleBookmarkState -> TODO()
            MediaDescriptionUserInteract.ToggleDislikeState -> TODO()
            MediaDescriptionUserInteract.ToggleLikeState -> TODO()
        }
    }
}
