package dam_51606.playedit.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import dam_51606.playedit.data.model.GameStatus
import dam_51606.playedit.data.model.UserGame
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Interacts with the user's Room DB database.
 */
class UserGameRepository {
    private val firestore = FirebaseFirestore.getInstance()

    /**
     * Gets the user's Firestore collection, where their data is stored.
     */
    private fun gameCollection(userId: String) = firestore.collection("users").document(userId).collection("games")

    // Fetching game/library info
    /**
     * Gets the user's whole game library, as a list of games.
     */
    fun getLibrary(userId: String): Flow<List<UserGame>> = callbackFlow {
        val listener = gameCollection(userId)
            .addSnapshotListener { snapshot, error -> // adds a listener on the firestore collection that will fire when data changes
                if (error != null) {
                    close(error); return@addSnapshotListener // terminates the flow if there's an error
                }
                trySend(snapshot?.toObjects(UserGame::class.java) ?: emptyList()) // Firestore document objects are converted tp USerGame and stored in list, then sent into the Flow
            }
        awaitClose { listener.remove() } // keeping the Flow until it's canceled, then removing the listener
    }

    /**
     * Gets all the games the user has set as favourites.
     */
    fun getFavorites(userId: String): Flow<List<UserGame>> = callbackFlow {
        val listener = gameCollection(userId)
            .whereEqualTo("favorite", true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                trySend(snapshot?.toObjects(UserGame::class.java) ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    /**
     * Gets all of the games that have a specific status.
     */
    fun getByStatus(userId: String, status: GameStatus): Flow<List<UserGame>> = callbackFlow {
        val listener = gameCollection(userId)
            .whereEqualTo("status", status.name)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                trySend(snapshot?.toObjects(UserGame::class.java) ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    /**
     * Fetches a game's data by its id.
     */
    fun getByGameId(userId: String, gameId: Int): Flow<UserGame?> = callbackFlow {
        val listener = gameCollection(userId)
            .whereEqualTo("gameId", gameId)
            .limit(1)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                trySend(snapshot?.toObjects(UserGame::class.java)?.firstOrNull())
            }
        awaitClose { listener.remove() }
    }


    // Adding, changing and deleting game data
    /**
     * Adds a game to the user's library.
     */
    suspend fun addGame(userGame: UserGame) {
        gameCollection(userGame.userId)
            .document(userGame.gameId.toString())
            .set(userGame)
            .await()
    }

    /**
     * Updates game information in the library.
     */
    suspend fun updateGame(userGame: UserGame) {
        gameCollection(userGame.userId)
            .document(userGame.gameId.toString())
            .set(userGame)
            .await()
    }

    /**
     * Removes a game from the user's library.
     */
    suspend fun removeGame(userGame: UserGame) {
        gameCollection(userGame.userId)
            .document(userGame.gameId.toString())
            .delete()
            .await()
    }


}