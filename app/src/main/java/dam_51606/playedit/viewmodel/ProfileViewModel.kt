package dam_51606.playedit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_51606.playedit.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {
    // UI state
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // Repository
    private val authRepository = AuthRepository()

    // Immediately loads page with present user data
    init {
        _uiState.update {
            it.copy(
                username = authRepository.currentUserName!!,
                email = authRepository.currentUserEmail!!,
                avatarUrl = authRepository.currentUserAvatar!!
            )
        }
    }

    /**
     * Updates the user's display name.
     * @param newDisplayName desired display name
     */
    fun updateUsername(newDisplayName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = authRepository.changeUsername(newDisplayName)
            _uiState.update {
                if (result.isSuccess) it.copy(isLoading = false, username = newDisplayName)
                else it.copy(isLoading = false, error = result.exceptionOrNull()?.message)
            }
        }
    }

    /**
     * Updates the user's avatar.
     * @param avatarUrl
     */
    fun updateAvatar(avatarUrl: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = authRepository.updateAvatar(avatarUrl)
            _uiState.update {
                if (result.isSuccess)
                    it.copy(isLoading = false, avatarUrl = avatarUrl)
                else
                    it.copy(isLoading = false, error = result.exceptionOrNull()?.message)
            }
        }
    }
}

/**
 * Data shown in a user's profile screen.
 * @param username
 * @param email
 * @param avatarUrl
 * @param isLoading
 * @param error
 */
data class ProfileUiState(
    val username: String = "",
    val email: String = "",
    val avatarUrl: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)