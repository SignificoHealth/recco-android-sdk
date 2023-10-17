package com.recco.internal.core.model.recommendation

import com.recco.api.model.ReccoStyle

data class User(
    val id: String,
    val isOnboardingQuestionnaireCompleted: Boolean,
    val reccoStyle: ReccoStyle? = null
)
