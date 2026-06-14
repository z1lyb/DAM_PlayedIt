package dam_51606.playedit.data.model

/**
 * Represents a game in the user's library,
 * including the score and review they left.
 * Stored in Firestore under users/id/games/gameId
 */
data class UserGame (
    // Game data
    val gameId: Int = 0, // RawG API game ID
    val genres: List<String> = emptyList(), // game genre names for stats
    val name: String = "", // game name
    val coverUrl: String = "", // cover picture
    // User data
    val userId: String = "",
    // Game status
    val status: GameStatus = GameStatus.NOT_STARTED,
    val isFavorite: Boolean = false,
    // Start and end dates
    val startDate: Long? = null,
    val endDate: Long? = null,
    // Evaluation
    val score: Int? = null,
    val review: String? = null,
)