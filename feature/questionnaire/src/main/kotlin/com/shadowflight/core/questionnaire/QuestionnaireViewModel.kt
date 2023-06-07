package com.shadowflight.core.questionnaire

import androidx.lifecycle.ViewModel
import com.shadowflight.core.logger.Logger
import com.shadowflight.core.repository.QuestionnaireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuestionnaireViewModel @Inject constructor(
    private val feedRepository: QuestionnaireRepository,
    private val logger: Logger
) : ViewModel() {
}