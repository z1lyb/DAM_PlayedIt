package dam_51606.playedit.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

/**
 * The AuthRepository class interacts with Firebase for login,
 * logout and registration purposes.
 */
class AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    val isLoggedIn: Boolean
        get() = auth.currentUser != null // if someone's already logged in

    val currentUser: FirebaseUser?
        get() = auth.currentUser // user currently logged in

    // Saved user data: id, username, avatar, email
    val currentUserId: String?
        get() = auth.currentUser?.uid ?: ""
    val currentUserEmail: String?
        get() = auth.currentUser?.email ?: ""
    val currentUserName: String?
        get() = auth.currentUser?.displayName ?: ""
    val currentUserAvatar: String?
        get() = auth.currentUser?.photoUrl?.toString() ?: ""

    /**
     * Registers a user to Firebase, using their email, password and username
     */
    suspend fun register(email: String, password: String, username: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user!!.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(username) // sets their username as a display name
                    .build()
            ).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Performs a user login, using their email and password
     */
    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user!!) // returns the user to log in as, in case of success
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Signs out of the current user.
     */
    fun logout() {
        auth.signOut()
    }

    /**
     * Changes an account's display name
     * @param newDisplayName desired new display name
     */
    suspend fun changeUsername(newDisplayName: String): Result<Unit> {
        return try {
            auth.currentUser!!.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(newDisplayName)
                    .build()
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Updates a user's avatar.
     * @param avatarUrl
     */
    suspend fun updateAvatar(avatarUrl: String): Result<Unit> {
        return try {
            auth.currentUser!!.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(avatarUrl))
                    .build()
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}