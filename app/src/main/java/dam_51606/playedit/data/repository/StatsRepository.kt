package dam_51606.playedit.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import dam_51606.playedit.data.model.GameStatus
import dam_51606.playedit.data.model.UserGame
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

/**
 * Fetches game stats from the related Room database.
 */
class StatsRepository {

    private val firestore = FirebaseFirestore.getInstance() // Firestore instance

    /**
     * Gets all games in the user's library, so their stats
     * can be calculated in the rest of the functions.
     */
    private fun allGames(userId: String): Flow<List<UserGame>> = callbackFlow {
         val listener = firestore.collection("users").document(userId)
            .collection("games")
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                trySend(snapshot?.toObjects(UserGame::class.java) ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    /**
     * Counts the amount of games the user has in their library.
     */
    fun getTotalGames(userId: String): Flow<Int> = allGames(userId).map { it.size }

    /**
     * Fetches and calculates the average score given.
     */
    fun getAverageScore(userId: String): Flow<Float?> = allGames(userId).map { games ->
        val scores = games.mapNotNull { it.score }
        if(scores.isEmpty()) null else scores.average().toFloat()
    }

    /**
     * Gets a status breakdown of the user's games, mapped by GameStatus: number of games(int)
     */
    fun getStatusBreakdown(userId: String): Flow<Map<GameStatus, Int>> = allGames(userId).map { games ->
        games.groupingBy { it.status }.eachCount()
    }

    /**
     * Gets a breakdown of the genres in the user's game library, mapped by genre: amount (int)
     */
    fun getGenreBreakdown(userId: String): Flow<Map<String, Int>> =
        allGames(userId).map { games ->
            games.flatMap { it.genres }
                .filter { it.isNotBlank() }
                .groupingBy { it }
                .eachCount()
        }
}