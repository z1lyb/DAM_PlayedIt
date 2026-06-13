package dam_51606.playedit.ui.navigation

/**
 * Defines the types of screens that can exist in the UI,
 * to then be used in the navigation graph.
 */
sealed class Screen(val route: String) {

    // Authentication
    object Login: Screen("login")
    object Register: Screen("register")

    // Profile
    object Profile: Screen("profile")
    object EditProfile: Screen("edit_profile")

    // Main navigation
    object Library: Screen("library")
    object Search: Screen("search")
    object Statistics: Screen("statistics")

    // Game details
    object GameDetails: Screen("game_detail/{gameId}") {
        fun createRoute(gameId: Int) = "game_detail/$gameId"
    }
    object EditGame: Screen("edit_game/{gameId}") {
        fun createRoute(gameId: Int) = "edit_game/$gameId"
    }
}