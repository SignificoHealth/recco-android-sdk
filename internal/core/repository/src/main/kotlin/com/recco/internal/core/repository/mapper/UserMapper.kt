package com.recco.internal.core.repository.mapper

import com.recco.internal.core.model.recommendation.User
import com.recco.internal.core.openapi.model.AppUserDTO

fun AppUserDTO.asEntity() = User(
    id = id,
    isOnboardingQuestionnaireCompleted = isOnboardingQuestionnaireCompleted
)