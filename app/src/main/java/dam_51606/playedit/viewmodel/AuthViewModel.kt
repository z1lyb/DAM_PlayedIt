package dam_51606.playedit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_51606.playedit.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    // UI state
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    // Repository + authentication data
    private val repository = AuthRepository()

    val isLoggedIn: Boolean get() = repository.isLoggedIn
    val currentUserId: String? get() = repository.currentUserId
    val currentUserEmail: String? get() = repository.currentUserEmail
    val currentUserName: String? get() = repository.currentUserName
    val currentUserAvatar: String? get() = repository.currentUserAvatar

    /**
     * Tries to log in a user, and updates the UI with the corresponding information.
     * @param email the user's email
     * @param password the user's password
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) } // sets the UI as loading while trying to log in
            val result = repository.login(email, password) // tries to log in using the repository
            _uiState.update {
                if (result.isSuccess) it.copy(isLoading = false, isAuthenticated = true) // if authentication is a success
                else it.copy(isLoading = false, error = result.exceptionOrNull()?.message) // if authentication fails
            }
        }
    }

    /**
     * Tries to register a user into the Firebase database.
     * @param email the user's email
     * @param password the user's password
     * @param username the desired username
     */
    fun register(email: String, password: String, username: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) } // sets UI to loading
            val result = repository.register(email, password, username) // tries to register
            _uiState.update {
                if (result.isSuccess) it.copy(isLoading = false, isAuthenticated = true) // if successful, sets as authenticated
                else it.copy(isLoading = false, error = result.exceptionOrNull()?.message) // if not, shows error message
            }
        }
    }

    /**
     * Logs a user out of their account.
     */
    fun logout() {
        repository.logout()
        _uiState.update { AuthUiState() } // sets the state to default
    }

    /**
     * Clears an error from the screen
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) } // sets the error to null
    }
}

/**
 * State of the authentication page UI - stores its loading,
 * authentication and error statuses, if existent.
 */
data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val error: String? = null
)