package dam_51606.playedit.data

/**
 * Represents the raw data of a game, with information from the RAWG.io API
 */
data class Game (
    val id: Int,
    val name: String,
    val genres: List<GameGenre>,
    val platforms: List<GamePlatform>,// platforms it's available in
    val developer: String?,
    val description: String?,
    val metacritic: Int?, // metacritic rating
    val released: Long?, // release date
    val bgImage: String?,
    val bgImageAlt: String?,
)

/**
 * Raw data of a game genre
 */
data class GameGenre(
    val id: Int,
    val name: String
)

/**
 * Raw data of a gaming platform
 */
data class GamePlatform(
    val id: Int,
    val name: String
)