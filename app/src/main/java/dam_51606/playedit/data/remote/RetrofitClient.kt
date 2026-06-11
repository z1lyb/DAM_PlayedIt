package dam_51606.playedit.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit singleton that builds and
 * exposes the configured RawgApiService.
 */
object RetrofitClient {
    private const val BASE_URL = "https://api.rawg.io/api/"

    val api: RawgApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RawgApiService::class.java)
    }
}