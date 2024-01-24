package com.recco.internal.feature.media.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recco.internal.core.logger.Logger
import com.recco.internal.core.model.recommendation.ContentId
import com.recco.internal.core.model.recommendation.ContentType
import com.recco.internal.core.model.recommendation.ContentType.AUDIO
import com.recco.internal.core.model.recommendation.ContentType.VIDEO
import com.recco.internal.core.repository.RecommendationRepository
import com.recco.internal.core.ui.components.UiState
import com.recco.internal.feature.media.video.navigation.idArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class FullMediaPlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recommendationRepository: RecommendationRepository,
    private val logger: Logger
) : ViewModel() {

    private val _viewState = MutableStateFlow(UiState<FullPlayerUI>())
    val viewState: Flow<UiState<FullPlayerUI>> = _viewState

    // TODO, remove
    val initialContentType: ContentType = VIDEO
    private val mediaId by lazy { checkNotNull(savedStateHandle.get<ContentId>(idArg)) }

    init {
        initialLoad()
    }

    private fun initialLoad() {
        viewModelScope.launch {
            runCatching { loadFullPlayerUI() }
                .onSuccess {
                    _viewState.value = UiState(
                        data = it,
                        isLoading = false,
                        error = null,
                    )
                }
                .onFailure {
                    _viewState.emit(UiState(error = it, isLoading = false))
                    logger.e(it)
                }
        }
    }

    private suspend fun loadFullPlayerUI(): FullPlayerUI {
        return when (initialContentType) {
            AUDIO -> FullPlayerUI.AudioUi(recommendationRepository.getAudio(mediaId))
            VIDEO -> FullPlayerUI.VideoUi(recommendationRepository.getVideo(mediaId))
            else -> error("Attempted to open the player with a $initialContentType content type")
        }
    }

    fun onUserInteract(userInteract: FullMediaPlayerUserInteract) {
        when (userInteract) {
            FullMediaPlayerUserInteract.Retry -> {
                initialLoad()
            }
            FullMediaPlayerUserInteract.ToggleBookmarkState -> TODO()
        }
    }
}
