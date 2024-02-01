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
import com.recco.internal.core.model.recommendation.UserInteractionRecommendation
import com.recco.internal.core.repository.RecommendationRepository
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.core.ui.components.toUiState
import com.recco.internal.feature.media.navigation.contentTypeArg
import com.recco.internal.feature.media.navigation.idArg
import com.recco.internal.feature.rating.delegates.ContentInteractViewModelDelegate
import com.recco.internal.feature.rating.delegates.ContentUserInteract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoadMediaViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val reccomendationRepository: RecommendationRepository,
    private val contentInteractViewModelDelegate: ContentInteractViewModelDelegate,
    private val logger: Logger
) : ViewModel() {
    private val _viewState = MutableStateFlow(UiState<MediaDescriptionUI>())
    private val contentId by lazy { checkNotNull(savedStateHandle.get<ContentId>(idArg)) }
    private val contentType by lazy { checkNotNull(savedStateHandle.get<ContentType>(contentTypeArg)) }
    val viewState: Flow<UiState<MediaDescriptionUI>> = _viewState

    val interactionViewState: Flow<UserInteractionRecommendation?> =
        contentInteractViewModelDelegate.viewState

    init {
        loadData()
    }

    fun onContentUserInteract(userInteract: ContentUserInteract) {
        contentInteractViewModelDelegate.onContentUserInteract(userInteract)
    }

    private fun loadData() {
        viewModelScope.launch {
            runCatching { loadMedia() }
                .onSuccess {
                    _viewState.value = it
                    contentInteractViewModelDelegate.userInteraction =
                        it.data?.asInteractionRecommendation()
                }
                .onFailure {
                    _viewState.value = it.toUiState()
                    logger.e(it)
                }
        }
    }

    private suspend fun loadMedia(): UiState<MediaDescriptionUI> {
        return when (contentType) {
            AUDIO -> reccomendationRepository.getAudio(contentId).toUiState()
            VIDEO -> reccomendationRepository.getVideo(contentId).toUiState()
            else -> error("Attempted to load $contentType in MediaDescriptionViewModel")
        }
    }

    private fun Audio.toUiState(): UiState<MediaDescriptionUI> {
        return UiState(
            isLoading = false,
            error = null,
            data = MediaDescriptionUI.AudioDescriptionUI(audio = this)
        )
    }

    private fun MediaDescriptionUI.asInteractionRecommendation(): UserInteractionRecommendation {
        return when (this) {
            is MediaDescriptionUI.AudioDescriptionUI -> {
                UserInteractionRecommendation(
                    contentId = audio.id,
                    rating = audio.rating,
                    isBookmarked = audio.isBookmarked
                )
            }
            is MediaDescriptionUI.VideoDescriptionUI -> {
                UserInteractionRecommendation(
                    contentId = video.id,
                    rating = video.rating,
                    isBookmarked = video.isBookmarked
                )
            }
        }
    }

    private fun Video.toUiState(): UiState<MediaDescriptionUI> {
        return UiState(
            isLoading = false,
            error = null,
            data = MediaDescriptionUI.VideoDescriptionUI(video = this)
        )
    }

    fun onUserInteract(mediaDescriptionUserInteract: MediaDescriptionUserInteract) {
        when (mediaDescriptionUserInteract) {
            MediaDescriptionUserInteract.Retry,
            MediaDescriptionUserInteract.InitialLoad -> {
                loadData()
            }
        }
    }

    override fun onCleared() {
        contentInteractViewModelDelegate.onCleared()
        super.onCleared()
    }
}
