package com.recco.internal.feature.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.recco.internal.feature.onboarding.OnboardingRoute

const val OnboardingGraph = "onboarding_graph"
const val OnboardingRoute = "onboarding"

fun NavGraphBuilder.onboardingGraph(
    navigateToQuestionnaire: () -> Unit
) {
    navigation(
        route = OnboardingGraph,
        startDestination = OnboardingRoute
    ) {
        composable(route = OnboardingRoute) {
            OnboardingRoute(navigateToQuestionnaire)
        }
    }
}
