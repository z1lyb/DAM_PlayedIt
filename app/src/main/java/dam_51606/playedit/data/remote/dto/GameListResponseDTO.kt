package dam_51606.playedit.data.remote.dto

/**
 * RAWG response of game list
 */
data class GameListResponseDTO (
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<GameDTO>
)