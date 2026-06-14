package dam_51606.playedit.data.remote.dto

/**
 * Data Transfer Object - for importing directly from the API.
 * Every attribute of the class is returned by the RAWG call and
 * stored in the data class for use in other classes.
 */
data class GameDTO (
    val id: Int,
    val name: String,
    val background_image: String?,
    val background_image_additional: String?,
    val genres: List<GameGenreDTO>,
    val platforms: List<GamePlatformWrapperDTO>,
    val metacritic: Int?,
    val released: String?,
    val description: String?,
    val developers: List<GameDeveloperDTO>?
)