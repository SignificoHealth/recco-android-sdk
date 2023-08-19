package com.recco.showcase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.recco.showcase.customize.CustomizePaletteRoute
import com.recco.showcase.login.LoginScreen
import com.recco.showcase.main.MainRoute

private const val MainRoute = "main"
private const val LoginRoute = "login"

const val paletteToEditIdArg = "paletteId"
private const val CreatePalette = "create_palette"
private const val EditPalette = "edit_palette/{$paletteToEditIdArg}"

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
            MainRoute(
                logoutClick = {
                    logout()
                    navController.navigate(LoginRoute) {
                        popUpTo(0)
                    }
                },
                openReccoClick = openReccoClick,
                createCustomPalette = { navController.navigate(CreatePalette) },
                editCustomPalette = { palette ->
                    navController.navigate(
                        EditPalette.replace(
                            oldValue = "{$paletteToEditIdArg}",
                            newValue = palette.id.toString()
                        )
                    )
                }
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

        composable(route = CreatePalette) {
            CustomizePaletteRoute(
                navigateUp = navController::navigateUp
            )
        }

        composable(route = EditPalette, arguments = listOf(
            navArgument(paletteToEditIdArg) { type = NavType.IntType }
        )) {
            CustomizePaletteRoute(
                navigateUp = navController::navigateUp
            )
        }
    }
}
