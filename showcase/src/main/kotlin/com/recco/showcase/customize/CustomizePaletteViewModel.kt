package com.recco.showcase.customize

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recco.showcase.data.ShowcaseRepository
import com.recco.showcase.models.ShowcasePalette
import com.recco.showcase.navigation.paletteToEditIdArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class CustomizePaletteViewModel @Inject constructor(
    private val repository: ShowcaseRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val paletteToEditId by lazy { savedStateHandle.get<Int>(paletteToEditIdArg) }
    private val isEditing by lazy { paletteToEditId != null }

    private val _viewState = MutableStateFlow(
        CustomizePaletteUI(
            palette = repository.getDefaultPalette()
                .let { palette ->
                    if (isEditing) palette else palette.copy(name = "")
                },
            isEditing = isEditing
        )
    )

    val viewState: StateFlow<CustomizePaletteUI> = _viewState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = CustomizePaletteUI(
            palette = repository.getDefaultPalette(),
            isEditing = isEditing
        )
    )

    init {
        viewModelScope.launch {
            paletteToEditId?.let { id ->
                _viewState.emit(_viewState.value.copy(palette = repository.getPalette(id)))
            }
        }
    }

    fun toggleMode() {
        viewModelScope.launch {
            _viewState.emit(_viewState.value.copy(isDarkMode = !_viewState.value.isDarkMode))
        }
    }

    fun onUpdate(palette: ShowcasePalette) {
        viewModelScope.launch {
            _viewState.emit(_viewState.value.copy(palette = palette))
        }
    }

    fun onSave(onSuccess: () -> Unit) {
        if (isEditing) {
            viewModelScope.launch {
                repository.updatePalette(_viewState.value.palette)
                onSuccess()
            }
        } else {
            viewModelScope.launch {
                repository.addPalette(_viewState.value.palette)
                onSuccess()
            }
        }
    }
}
