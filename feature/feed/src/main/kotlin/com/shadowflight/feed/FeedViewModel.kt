package com.shadowflight.feed

import androidx.lifecycle.ViewModel
import com.shadowflight.repository.MeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val meRepository: MeRepository
) : ViewModel() {

    fun getMyId(): Flow<FeedViewUIState> =
        meRepository.getMyId().map { FeedViewUIState(userId = it) }
}

data class FeedViewUIState(
    val userId: String = ""
)