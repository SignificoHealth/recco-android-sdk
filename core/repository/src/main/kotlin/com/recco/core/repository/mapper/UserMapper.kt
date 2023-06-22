package com.recco.core.repository.mapper

import com.recco.core.model.recommendation.User
import com.recco.core.openapi.model.AppUserDTO

fun AppUserDTO.asEntity() = User(
    id = id,
    isOnboardingQuestionnaireCompleted = isOnboardingQuestionnaireCompleted
)