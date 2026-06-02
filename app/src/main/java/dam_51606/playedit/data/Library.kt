package dam_51606.playedit.data

/**
 * Represents a user's game library.
 */
data class Library (
    val userID: Int,
    val gameList: List<UserGame>
)