package dam_51606.playedit.data.local

/**
 * Represents a game in the user's library,
 * including the score and review they left.
 */
data class UserGame (
    val gameId: Int,
    val userId: Int,
    val status: GameStatus,
    val startDate: Long,
    val endDate: Long,
    val score: Int,
    val review: String,
    val isFavorite: Boolean
)