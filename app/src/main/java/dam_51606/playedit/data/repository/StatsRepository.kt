package dam_51606.playedit.data.repository

import dam_51606.playedit.data.local.GameStatus
import dam_51606.playedit.data.local.dao.UserGameDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Fetches game stats from the related Room database.
 */
class StatsRepository(private val dao: UserGameDAO) {

    fun getAverageScore(userId: String): Flow<Float?> = dao.getAverageScore(userId)

    fun getTotalGames(userId: String): Flow<Int> = dao.getGameCount(userId)

    fun countByStatus(userId: String, status: GameStatus): Flow<Int> = dao.countByStatus(userId,status)

    /**
     * Returns a breakdown of the genres in the user's library.
     */
    fun getGenreBreakdown(userId: String): Flow<Map<String, Int>> =
        dao.getAllGenres(userId).map {genreList ->
            genreList
                .flatMap { it.split(",") }
                .filter { it.isNotBlank() }
                .groupingBy { it }
                .eachCount()
        }
}