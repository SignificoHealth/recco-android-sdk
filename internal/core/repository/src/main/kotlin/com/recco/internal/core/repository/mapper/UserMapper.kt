package com.recco.internal.core.repository.mapper

import com.recco.internal.core.model.recommendation.User
import com.recco.internal.core.openapi.model.AppUserDTO

internal fun AppUserDTO.asEntity(): User {
    return User(
        id = id,
        isOnboardingQuestionnaireCompleted = isOnboardingQuestionnaireCompleted,
        reccoStyle = appStyle?.asEntity()
    )
}
