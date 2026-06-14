package dam_51606.playedit.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.ui.graphics.vector.ImageVector

/**
 *
 */
sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Library : BottomNavItem(Screen.Library.route, Icons.Default.VideoLibrary, "Library")
    object Search : BottomNavItem(Screen.Search.route, Icons.Default.Search, "Search")
    object Profile : BottomNavItem(Screen.Profile.route, Icons.Default.Person, "Profile")
}