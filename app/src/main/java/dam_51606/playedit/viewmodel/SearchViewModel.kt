package dam_51606.playedit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam_51606.playedit.data.model.Game
import dam_51606.playedit.data.repository.GameRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {
    // UI state
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    // Repository
    private val gameRepository = GameRepository()

    // background job coroutine for fetching game data
    private var searchJob: Job? = null

    /**
     * Updates the results depending on the search term inserted.
     * @param query search term
     */
    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }

        searchJob?.cancel()
        if(query.isBlank()) {
            _uiState.update { it.copy(results = emptyList(), isLoading = false) }
            return
        }

        searchJob = viewModelScope.launch {
            delay(500) // waits half a second before searching
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = gameRepository.searchGames(query)
            _uiState.update {
                if(result.isSuccess) it.copy(isLoading = false, results = result.getOrDefault(emptyList()))
                else it.copy(isLoading = false, error = result.exceptionOrNull()?.message)
            }
        }
    }

    /**
     * Clears the search bar.
     */
    fun clearSearch() {
        searchJob?.cancel()
        _uiState.update { SearchUiState() }
    }

}

/**
 * State of the search screen.
 * @param query search term
 * @param results available results
 * @param isLoading if the page is loading
 * @param error error message, if available
 */
data class SearchUiState(
    val query: String = "",
    val results: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)