package dam_51606.playedit.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dam_51606.playedit.ui.screens.auth.LoginScreenUI
import dam_51606.playedit.ui.screens.auth.RegisterScreenUI
import dam_51606.playedit.ui.screens.main.LibraryScreenUI

/**
 * Application navigation graph - defines the routes a
 * user can take to access certain pages.
 * @param navController central navigation API
 * @param startDestination where the application starts
 */
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // authentication
        composable(Screen.Login.route) {
            LoginScreenUI(
                onRegisterButtonClick = { navController.navigate(Screen.Register.route) },
                onLoggedIn = { navController.navigate(Screen.Library.route) }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreenUI(
                onRegistered = { navController.navigate(Screen.Library.route) },
                onLoginButtonClick = { navController.navigate(Screen.Login.route) }
            )
        }

        // profile
        composable(Screen.Profile.route) {
            Text("Profile screen")
        }
        composable(Screen.EditProfile.route) {
            Text("Profile edition screen")
        }

        // main
        composable(Screen.Library.route) {
            LibraryScreenUI(
                onGameClick = { gameId ->
                    navController.navigate(Screen.GameDetails.createRoute(gameId))
                }
            )
        }
        composable(Screen.Search.route) {
            Text("Search screen")
        }
        composable(Screen.Statistics.route) {
            Text("Stats screen")
        }

        // game details
        composable(
            Screen.GameDetails.route,
            arguments = listOf(navArgument("gameId") { type = NavType.IntType })
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getInt("gameId") ?: return@composable
            Text("Game Detail: $gameId")
        }

        composable(
            Screen.EditGame.route,
            arguments = listOf(navArgument("gameId") { type = NavType.IntType })
        ) { backstackEntry ->
            val gameId = backstackEntry.arguments?.getInt("gameId") ?: return@composable
            Text("Edit Game: $gameId")
        }
    }
}