package com.recco.showcase.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recco.api.model.ReccoFont
import com.recco.showcase.data.ShowcaseRepository
import com.recco.showcase.models.ShowcasePalette
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ShowcaseRepository
) : ViewModel() {
    private val _viewState = MutableStateFlow(
        MainUI(
            palettes = emptyList(),
            selectedPaletteId = repository.getSelectedPaletteId(),
            selectedFont = repository.getSelectedReccoFont()
        )
    )

    val viewState: StateFlow<MainUI> = _viewState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = MainUI()
    )

    init {
        viewModelScope.launch {
            repository.getPalettes().collectLatest { palettes ->
                _viewState.emit(
                    _viewState.value.copy(
                        palettes = palettes,
                        selectedPaletteId = repository.getSelectedPaletteId()
                    )
                )
            }
        }
    }

    fun setSelectedReccoFont(font: ReccoFont) {
        viewModelScope.launch {
            repository.setReccoFont(font)
            _viewState.emit(_viewState.value.copy(selectedFont = font))
        }
    }

    fun setSelectedPaletteId(paletteId: Int) {
        viewModelScope.launch {
            repository.setSelectedPaletteId(paletteId)
            _viewState.emit(_viewState.value.copy(selectedPaletteId = paletteId))
        }
    }

    fun deleteCustomPalette(palette: ShowcasePalette) {
        viewModelScope.launch {
            repository.deletePalette(palette)
        }
    }
}
