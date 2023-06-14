package com.shadowflight.core.questionnaire

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.shadowflight.core.ui.components.UiState
import com.shadowflight.core.ui.preview.QuestionnairePreviewProvider

class QuestionnaireUIStatePreviewProvider :
    PreviewParameterProvider<UiState<QuestionnaireUI>> {
    override val values
        get() = sequenceOf(
            UiState(
                isLoading = false,
                data = QuestionnaireUI(
                    questions = QuestionnairePreviewProvider.multiChoice(maxOptions = 2),
                    progress = .25f,
                    isLastPage = true,
                    isNextEnabled = true,
                    showBack = true
                )
            ),
            UiState(
                isLoading = false,
                data = QuestionnaireUI(
                    questions = QuestionnairePreviewProvider.multiChoice(maxOptions = 1),
                    progress = .5f
                )
            ),
            UiState(
                isLoading = false,
                data = QuestionnaireUI(
                    questions = QuestionnairePreviewProvider.numeric(),
                    progress = .75f
                )
            ),
            UiState(
                isLoading = true
            ),
            UiState(
                isLoading = false,
                error = Throwable()
            )
        )
}