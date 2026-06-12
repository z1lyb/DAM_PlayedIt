package dam_51606.playedit.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dam_51606.playedit.data.local.GameStatus
import dam_51606.playedit.data.local.UserGame
import kotlinx.coroutines.flow.Flow

@Dao // Data Access Object - interactions to get games from a user's "user_games" table
interface UserGameDAO {

    /**
     * Inserts a game into the user's library.
     * OnConflictStrategy indicates that if there's two games with the same ID,
     * the inserted one will replace the present one.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userGame: UserGame)

    /**
     * Deletes a game from the user's library.
     */
    @Delete
    suspend fun delete(userGame: UserGame)

    /**
     * Updates a game entry.
     */
    @Update
    suspend fun update(userGame: UserGame)

    /**
     * Fetches the user's entire library.
     */
    @Query("SELECT * FROM user_games WHERE userId = :userId")
    fun getAll(userId: String): Flow<List<UserGame>>

    /**
     * Fetches a user's favorite games.
     */
    @Query("SELECT * FROM user_games WHERE userId= :userId AND isFavorite= 1")
    fun getFavorites(userId: String): Flow<List<UserGame>>

    /**
     * Fetches a game from the library by ID.
     */
    @Query("SELECT * FROM user_games WHERE id= :id")
    fun getById(id: Int): Flow<UserGame?>

    /**
     * Fetches a game from the library by RAWG ID.
     * (checks if it's in the user's library)
     */
    @Query("SELECT * FROM user_games WHERE userId= :userId AND gameId= :rawgId")
    fun getById(userId: String, rawgId: Int): Flow<UserGame?>

    /**
     * Gets games in a user's library by their status
     */
    @Query("SELECT * FROM user_games WHERE userId= :userId AND status= :status")
    fun getByStatus(userId: String, status: GameStatus): Flow<List<UserGame>>

    /**
     * Gets the number of games in the user's library.
     */
    @Query("SELECT COUNT(*) FROM user_games WHERE userId= :userId")
    fun getGameCount(userId: String): Flow<Int>

    /**
     * Counts the games with a particular status.
     */
    @Query("SELECT COUNT(*) FROM user_games WHERE userId = :userId AND status = :status")
    fun countByStatus(userId: String, status: GameStatus): Flow<Int>

    /**
     * Gets the user's average given score.
     */
    @Query("SELECT AVG(score) FROM user_games WHERE userId= :userId AND score IS NOT NULL")
    fun getAverageScore(userId: String): Flow<Float?>

    /**
     * Gets all genres in a user's library.
     */
    @Query("SELECT genres FROM user_games WHERE userId = :userId")
    fun getAllGenres(userId: String): Flow<List<String>>
}