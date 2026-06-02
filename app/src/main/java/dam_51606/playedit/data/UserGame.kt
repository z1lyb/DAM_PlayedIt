package dam_51606.playedit.data

/**
 * Represents a game in the user's library.
 */
data class UserGame (
    val gameId: Int,
    val userId: Int,
    val status: GameStatus,
    val startDate: Long,
    val endDate: Long,
    val evaluation: Evaluation,
    val isFavorite: Boolean
)