package dam_51606.playedit.data

/**
 * Represents a review left by a user on a game.
 */
class Evaluation (
    val id: Int,
    val gameId: Int,
    val userId: Int,
    val score: Float,
    val review: String?
)