package dam_51606.playedit.data.remote

import dam_51606.playedit.data.model.Game
import dam_51606.playedit.data.model.GameDeveloper
import dam_51606.playedit.data.model.GameGenre
import dam_51606.playedit.data.model.GamePlatform
import dam_51606.playedit.data.remote.dto.GameDTO
import dam_51606.playedit.data.remote.dto.GameDeveloperDTO
import dam_51606.playedit.data.remote.dto.GamePlatformDTO
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Maps API objects(DTOs) into model objects (Game, etc)
 */
object RawgMapper {
    /**
     * Maps a GameDTO object into a Game object.
     */
    fun toDomain(dto: GameDTO): Game = Game(
        dto.id,
        dto.name,
        dto.genres.map { GenreDTO ->
            GameGenre(GenreDTO.id, GenreDTO.name) },
        dto.platforms.map { GamePlatformDTO ->
            GamePlatform(GamePlatformDTO.id, GamePlatformDTO.name) },
        dto.developers?.map { GameDeveloperDTO ->
            GameDeveloper(GameDeveloperDTO.id, GameDeveloperDTO.name)},
        dto.description,
        dto.metacritic,
        dto.released,
        dto.background_image,
        dto.background_image_additional
    )

    /**
     * Converts an entire game list into Game objects.
     */
    fun toDomainList(dtos: List<GameDTO>): List<Game> = dtos.map { toDomain(it) }
}