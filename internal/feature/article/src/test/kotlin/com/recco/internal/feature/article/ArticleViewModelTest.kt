package com.recco.internal.feature.article

import androidx.lifecycle.SavedStateHandle
import com.recco.internal.core.logger.Logger
import com.recco.internal.core.repository.RecommendationRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class ArticleViewModelTest {
    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    private val savedStateHandle = mock<SavedStateHandle> {
    }

    private val repository = mock<RecommendationRepository> {
    }

    private val logger = mock<Logger> {
    }

    @Test
    fun dummy_test() {
        ArticleViewModel(savedStateHandle, repository, logger)
    }
}