package dam_51606.playedit.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dam_51606.playedit.ui.components.mainapp.GameCard
import dam_51606.playedit.viewmodel.SearchViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import dam_51606.playedit.ui.components.mainapp.SearchBar
import dam_51606.playedit.ui.components.mainapp.SearchEmptyState

@Composable
fun SearchScreenUI(
    viewModel: SearchViewModel = viewModel(),
    onGameClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    // same layout for both orientations
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SearchBar(
            query = uiState.query,
            focusRequester = focusRequester,
            onQueryChange = { viewModel.onQueryChange(it) },
            onClearSearch = { viewModel.clearSearch() }
        )

        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.results.isNotEmpty() -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.results) { game ->
                        GameCard(
                            gameName = game.name,
                            coverUrl = game.bgImage,
                            status = null,
                            onClick = { onGameClick(game.id) }
                        )
                    }
                }
            }
            else -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    SearchEmptyState(uiState = uiState)
                }
            }
        }
    }
}