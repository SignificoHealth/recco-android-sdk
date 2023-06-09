package com.shadowflight.core.article

import com.shadowflight.core.model.recommendation.Article

data class ArticleUI(
    val article: Article,
    val userInteraction: UserInteractionRecommendation
)