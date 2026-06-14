package dam_51606.playedit.data.repository

import android.util.Log
import dam_51606.playedit.data.model.Game
import dam_51606.playedit.data.remote.RawgMapper
import dam_51606.playedit.data.remote.RetrofitClient

/**
 * Fetches game information from the RAWG API.
 */
class GameRepository {
    private val api = RetrofitClient.api

    /**
     * Searches for games from the API.
     */
    suspend fun searchGames(query: String): Result<List<Game>> {
        return try { // maps the JSON response to Game objects
            val response = api.searchGames(query = query)
            Result.success(RawgMapper.toDomainList(response.results))
        } catch (e: Exception) {
            Log.e("GameRepository", "Search failed: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Searches the API for game details.
     */
    suspend fun getGameDetails(gameId: Int): Result<Game> {
        return try { // maps the API JSON response to a Game object
            val response = api.getGameDetail(gameId)
            Result.success(RawgMapper.toDomain(response))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}