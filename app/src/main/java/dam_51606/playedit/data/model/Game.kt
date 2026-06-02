package dam_51606.playedit.data.model

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