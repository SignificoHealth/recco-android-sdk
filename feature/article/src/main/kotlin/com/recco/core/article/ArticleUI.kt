package com.recco.core.article

import com.recco.core.model.recommendation.Article

data class ArticleUI(
    val article: Article,
    val userInteraction: UserInteractionRecommendation
)