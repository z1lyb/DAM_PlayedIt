package dam_51606.playedit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_51606.playedit.data.model.GameStatus
import dam_51606.playedit.data.model.User
import dam_51606.playedit.data.model.UserGame
import dam_51606.playedit.data.repository.AuthRepository
import dam_51606.playedit.data.repository.UserGameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameLibraryViewModel: ViewModel() {
    // UI state
    private val _uiState = MutableStateFlow(GameLibraryUiState())
    val uiState: StateFlow<GameLibraryUiState> = _uiState.asStateFlow()

    // Repositories
    private val gameRepository = UserGameRepository()
    private val authRepository = AuthRepository()

    init { // the library is loaded when the view is launched
        loadLibrary()
    }

    /**
     * Loads the current state of the user's library.
     */
    private fun loadLibrary() {
        viewModelScope.launch {
            val userId = authRepository.currentUserId

            // fetches the user's games stored in Firestore
            gameRepository.getLibrary(userId!!).collect { games ->
                _uiState.update { it.copy(games = games, isLoading = false) }
            }
        }
    }

    /**
     * Filters the library by game status.
     */
    fun filterByStatus(status: GameStatus?) {
        _uiState.update { it.copy(activeFilter = status) }
    }

    /**
     * Searches for a game within the library.
     */
    fun searchLibrary(searchQuery: String) {
        _uiState.update { it.copy(searchQuery = searchQuery) }
    }

    /**
     * Filters the games from the searched term and selected game status.
     */
    val filteredGames: StateFlow<List<UserGame>> = uiState.map { state ->
        var result = state.games
        state.activeFilter?.let { filter -> result = result.filter { it.status == filter } } // filters by status
        if (state.searchQuery.isNotBlank())
            result = result.filter { it.gameId.toString().contains(state.searchQuery) } // filters by search query
        result
    // keeps flow alive for 5s after the last collection to avoid having to restart
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

/**
 * State of the game library UI.
 */
data class GameLibraryUiState(
    val isLoading: Boolean = true,
    val games: List<UserGame> = emptyList(),
    val activeFilter: GameStatus? = null,
    val searchQuery: String = "",
    val error: String? = null
)