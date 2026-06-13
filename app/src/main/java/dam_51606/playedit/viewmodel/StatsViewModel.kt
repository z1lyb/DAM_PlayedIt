package dam_51606.playedit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_51606.playedit.data.model.GameStatus
import dam_51606.playedit.data.repository.AuthRepository
import dam_51606.playedit.data.repository.StatsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatsViewModel: ViewModel() {
    // UI state
    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    // Repositories
    private val authRepository = AuthRepository()
    private val statsRepository = StatsRepository()

    // Loads user's stats when page starts running
    init {
        loadStats()
    }

    /**
     * Loads all presented stats into the UI state.
     */
    private fun loadStats() {
        val userId = authRepository.currentUserId!! // assert there is a userId obtained
        viewModelScope.launch {
            // concurrently gets all needed user data
            launch {
                statsRepository.getTotalGames(userId).collect { total ->
                    _uiState.update { it.copy(totalGames = total) } }
            }
            launch {
                statsRepository.getAverageScore(userId).collect { average ->
                    _uiState.update { it.copy(averageScore = average) }
                }
            }
            launch {
                statsRepository.getStatusBreakdown(userId).collect { breakdown ->
                    _uiState.update { it.copy(statusBreakdown = breakdown) }
                }
            }
            launch {
                statsRepository.getGenreBreakdown(userId).collect { breakdown ->
                    _uiState.update { it.copy(genreBreakdown = breakdown) }
                }
            }
        }
    }

}

/**
 * Stores a user's stats.
 * @param totalGames total games in library
 * @param averageScore average score given to games
 * @param statusBreakdown number of games in each state
 * @param genreBreakdown number of games of each genre
 */
data class StatsUiState(
    val totalGames: Int = 0,
    val averageScore: Float? = null,
    val statusBreakdown: Map<GameStatus, Int> = emptyMap(),
    val genreBreakdown: Map<String, Int> = emptyMap()
)