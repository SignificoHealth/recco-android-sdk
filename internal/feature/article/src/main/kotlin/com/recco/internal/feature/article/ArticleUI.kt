package com.recco.internal.feature.article

import com.recco.internal.core.model.recommendation.Article
import com.recco.internal.core.ui.components.UserInteractionRecommendation

internal data class ArticleUI(
    val article: Article,
    val userInteraction: UserInteractionRecommendation
)
