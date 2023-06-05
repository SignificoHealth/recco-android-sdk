package com.shadowflight.core.onboarding.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.shadowflight.core.onboarding.OnboardingRoute

const val OnboardingGraph = "onboarding_graph"
const val OnboardingRoute = "onboarding"

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.onboardingGraph() {
    navigation(
        route = OnboardingGraph,
        startDestination = OnboardingRoute
    ) {
        composable(route = OnboardingRoute) {
            OnboardingRoute()
        }
    }
}
