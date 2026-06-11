package dam_51606.playedit.data.remote

import dam_51606.playedit.BuildConfig
import dam_51606.playedit.data.remote.dto.GameDTO
import dam_51606.playedit.data.remote.dto.GameListResponseDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RawgApiService {

    // Search API for games
    @GET("games")
    suspend fun searchGames(
        @Query("key") apiKey: String = BuildConfig.API_KEY,
        @Query("search") query: String,
        @Query("page_size") pageSize: Int = 30
    ): GameListResponseDTO

    // search API for specific gme details
    @GET("games/{id}")
    suspend fun getGameDetail(
        @Path("id") gameId: Int,
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ): GameDTO
}