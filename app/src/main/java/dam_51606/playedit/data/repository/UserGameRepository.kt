package dam_51606.playedit.data.repository

import dam_51606.playedit.data.local.GameStatus
import dam_51606.playedit.data.local.UserGame
import dam_51606.playedit.data.local.dao.UserGameDAO
import kotlinx.coroutines.flow.Flow

/**
 * Interacts with the user's Room DB database.
 */
class UserGameRepository(private val dao: UserGameDAO) {

    // Fetching game/library info
    fun getLibrary(userId: String): Flow<List<UserGame>> = dao.getAll(userId)
    fun getById(userId: String, id: Int): Flow<UserGame?> = dao.getById(id)
    fun getFavorites(userId: String): Flow<List<UserGame>> = dao.getFavorites(userId)
    fun getByStatus(userId: String, status: GameStatus): Flow<List<UserGame>> = dao.getByStatus(userId, status)

    // Adding, changing and deleting game data
    suspend fun addGame(userGame: UserGame) = dao.insert(userGame)
    suspend fun updateGame(userGame: UserGame) = dao.update(userGame)
    suspend fun removeGame(userGame: UserGame) = dao.delete(userGame)

}