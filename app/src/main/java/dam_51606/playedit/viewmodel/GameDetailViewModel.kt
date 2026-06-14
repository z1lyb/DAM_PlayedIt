package dam_51606.playedit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_51606.playedit.data.repository.AuthRepository
import dam_51606.playedit.data.repository.GameRepository
import dam_51606.playedit.data.repository.UserGameRepository
import dam_51606.playedit.data.model.Game
import dam_51606.playedit.data.model.GameStatus
import dam_51606.playedit.data.model.UserGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameDetailViewModel: ViewModel() {

    // UI state
    private val _uiState = MutableStateFlow(GameDetailUiState())
    val uiState: StateFlow<GameDetailUiState> = _uiState.asStateFlow()

    // Repositories
    private val authRepository = AuthRepository()
    private val gameRepository = GameRepository()
    private val userGameRepository = UserGameRepository()

    /**
     * Loads a game's details into the UI state.
     */
    fun loadGame(gameId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // fetching game details from the API
            val gameResult = gameRepository.getGameDetails(gameId)
            if(gameResult.isFailure) {
                _uiState.update { it.copy(isLoading = false, error = gameResult.exceptionOrNull()?.message) } // (if there's no game, present error message)
                return@launch
            }

            val game = gameResult.getOrNull()!!
            _uiState.update { it.copy(game = game) }

            // checking if the game is in the user's library
            val userId = authRepository.currentUserId!!
            userGameRepository.getByGameId(userId, gameId).collect { userGame ->
                _uiState.update { it.copy(isLoading = false, userGame = userGame) }
            }
        }
    }

    /**
     * Adds the game to the user's library.
     */
    fun addToLibrary(game: Game) {
        viewModelScope.launch {
            val userId = authRepository.currentUserId!!
            val userGame = UserGame(
                game.id,
                game.genres.map { it.name },
                game.name,
                game.bgImage!!,
                userId
            )
            userGameRepository.addGame(userGame)
        }
    }

    /**
     * Removes the game from the user's library.
     */
    fun removeFromLibrary() {
        viewModelScope.launch {
            _uiState.value.userGame?.let { userGameRepository.removeGame(it) }
        }
    }

    /**
     * Updates the game's status for the current user.
     * @param status desired game status
     */
    fun updateStatus(status: GameStatus) {
        viewModelScope.launch {
            _uiState.value.userGame?.let {
                userGameRepository.updateGame(it.copy(status = status))
            }
        }
    }

    /**
     * Toggles the game's status as one of the user's favorites
     */
    fun toggleFavorite() {
        viewModelScope.launch {
            _uiState.value.userGame?.let {
                userGameRepository.updateGame(it.copy(isFavorite = !it.isFavorite))
            }
        }
    }

    /**
     * Adds an evaluation (score + review) of the viewed game.
     * @param score score to evaluate game
     * @param review review to leave
     */
    fun addEvaluation(score: Int, review: String) {
        viewModelScope.launch {
            _uiState.value.userGame?.let {
                userGameRepository.updateGame(it.copy(score = score, review = review))
            }
        }
    }
}

/**
 * Details related to a game
 * to show on its respective screen.
 */
data class GameDetailUiState(
    val isLoading: Boolean = true,
    val game: Game? = null,
    val userGame: UserGame? = null,
    val error: String? = null
)