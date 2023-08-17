package com.recco.showcase.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recco.api.model.ReccoFont
import com.recco.api.model.ReccoPalette
import com.recco.showcase.ShowcaseRepository
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
class MainViewModel @Inject constructor(
    private val repository: ShowcaseRepository
) : ViewModel() {
    private val _viewState = MutableStateFlow(
        MainUI(
            selectedPalette = repository.getSelectedReccoPalette(),
            selectedFont = repository.getSelectedReccoFont()
        )
    )

    val viewState: StateFlow<MainUI> = _viewState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
        initialValue = MainUI()
    )

    val selectedReccoPalette: ReccoPalette get() = repository.getSelectedReccoPalette()

    val selectedReccoFont: ReccoFont get() = repository.getSelectedReccoFont()

    fun setReccoFont(font: ReccoFont) {
        repository.setReccoFont(font)
        viewModelScope.launch {
            _viewState.emit(_viewState.value.copy(selectedFont = font))
        }
    }

    fun setReccoPalette(palette: ReccoPalette) {
        repository.setReccoPalette(palette)
        viewModelScope.launch {
            _viewState.emit(_viewState.value.copy(selectedPalette = palette))
        }
    }
}
