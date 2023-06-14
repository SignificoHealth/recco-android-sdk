package com.shadowflight.core.repository.mapper

import com.shadowflight.core.model.recommendation.User
import com.shadowflight.core.openapi.model.AppUserDTO

fun AppUserDTO.asEntity() = User(
    id = id,
    isOnboardingQuestionnaireCompleted = isOnboardingQuestionnaireCompleted
)