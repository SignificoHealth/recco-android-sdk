package com.shadowflight.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.shadowflight.article.navigation.articleGraph
import com.shadowflight.feed.navigation.FeedGraph
import com.shadowflight.feed.navigation.FeedRoute
import com.shadowflight.feed.navigation.feedGraph
import com.shadowflight.onboarding.navigation.onboardingGraph

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = FeedGraph
    ) {
        feedGraph()
        onboardingGraph()
        articleGraph()
    }
}