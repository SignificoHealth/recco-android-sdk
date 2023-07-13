package com.recco.showcase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.recco.showcase.customize.CustomizeScreen
import com.recco.showcase.login.LoginScreen
import com.recco.showcase.main.MainScreen

private const val MainRoute = "main"
private const val LoginRoute = "login"
private const val CustomizeRoute = "customize"

@Composable
fun ShowcaseNavHost(
    modifier: Modifier = Modifier,
    updateClientId: (String) -> Unit,
    logout: () -> Unit,
    openReccoClick: () -> Unit,
    isUserLoggedIn: Boolean,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = if (isUserLoggedIn) MainRoute else LoginRoute
    ) {
        composable(route = MainRoute) {
            MainScreen(
                logoutClick = {
                    logout()
                    navController.navigate(LoginRoute) {
                        popUpTo(0)
                    }
                },
                openReccoClick = openReccoClick,
                navigateToCustomizeScreen = { navController.navigate(CustomizeRoute) }
            )
        }

        composable(route = LoginRoute) {
            LoginScreen(
                updateClientId = updateClientId,
                loginClick = {
                    navController.navigate(MainRoute) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(route = CustomizeRoute) {
            CustomizeScreen(
                navigateUp = navController::navigateUp
            )
        }
    }
}