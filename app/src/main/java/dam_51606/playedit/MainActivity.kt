package dam_51606.playedit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dam_51606.playedit.data.repository.AuthRepository
import dam_51606.playedit.ui.components.BottomBar
import dam_51606.playedit.ui.navigation.NavGraph
import dam_51606.playedit.ui.navigation.Screen
import dam_51606.playedit.ui.theme.AppTheme

/**
 * Main app activity, defines what page the application starts in
 * depending on if the user has logged into their account.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                val authRepository = AuthRepository() // for checking if the user's logged in

                val startDestination = if (authRepository.isLoggedIn) Screen.Library.route else Screen.Login.route

                val bottomBarRoutes = listOf(
                    Screen.Library.route,
                    Screen.Search.route,
                    Screen.Profile.route
                )

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val showBottomBar = currentRoute in bottomBarRoutes

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) BottomBar(navController = navController)
                    }
                ) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}