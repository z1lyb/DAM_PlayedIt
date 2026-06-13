package dam_51606.playedit.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dam_51606.playedit.ui.screens.auth.LoginScreenUI
import dam_51606.playedit.ui.screens.auth.RegisterScreenUI

/**
 * Application navigation graph - defines the routes a
 * user can take to access certain pages.
 * @param navController central navigation API
 * @param startDestination where the application starts
 */
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // authentication
        composable(Screen.Login.route) {
            LoginScreenUI()
        }
        composable(Screen.Register.route) {
            RegisterScreenUI()
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
            Text("Library screen")
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