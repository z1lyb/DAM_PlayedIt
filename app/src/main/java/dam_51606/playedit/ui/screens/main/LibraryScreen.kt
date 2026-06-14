package dam_51606.playedit.ui.screens.main

import android.content.res.Configuration
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dam_51606.playedit.data.model.GameStatus
import dam_51606.playedit.data.model.UserGame
import dam_51606.playedit.ui.components.GameCard
import dam_51606.playedit.viewmodel.GameLibraryUiState
import dam_51606.playedit.viewmodel.GameLibraryViewModel


@Composable
fun LibraryScreenUI(
    viewModel: GameLibraryViewModel = viewModel(),
    onGameClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val filteredGames by viewModel.filteredGames.collectAsState()

    val screenConfig = LocalConfiguration.current
    if (screenConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
        PortraitLibraryUI(
            uiState = uiState,
            filteredGames = filteredGames,
            onGameClick = onGameClick,
            onSearchChange = { viewModel.searchLibrary(it) },
            onFilterChange = { viewModel.filterByStatus(it) },
            onFavoritesToggle = { viewModel.showFavoritesOnly(it) }
        )
    } else {
        LandscapeLibraryUI(
            uiState = uiState,
            filteredGames = filteredGames,
            onGameClick = onGameClick,
            onSearchChange = { viewModel.searchLibrary(it) },
            onFilterChange = { viewModel.filterByStatus(it) },
            onFavoritesToggle = { viewModel.showFavoritesOnly(it) }
        )
    }
}

@Composable
fun PortraitLibraryUI(
    uiState: GameLibraryUiState,
    filteredGames: List<UserGame>,
    onGameClick: (Int) -> Unit,
    onSearchChange: (String) -> Unit,
    onFilterChange: (GameStatus?) -> Unit,
    onFavoritesToggle: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // search bar
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search library...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // filter chips
        FilterRow(
            activeFilter = uiState.activeFilter,
            favoritesOnly = uiState.favoritesOnly,
            onFilterChange = onFilterChange,
            onFavoritesToggle = onFavoritesToggle
        )

        // content
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            filteredGames.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No games found.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filteredGames) { userGame ->
                        GameCard(
                            gameName = userGame.name,
                            coverUrl = userGame.coverUrl,
                            status = userGame.status,
                            isFavorite = userGame.isFavorite,
                            score = userGame.score,
                            onClick = { onGameClick(userGame.gameId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LandscapeLibraryUI(
    uiState: GameLibraryUiState,
    filteredGames: List<UserGame>,
    onGameClick: (Int) -> Unit,
    onSearchChange: (String) -> Unit,
    onFilterChange: (GameStatus?) -> Unit,
    onFavoritesToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // left sidebar — search + filters
        Column(
            modifier = Modifier
                .width(200.dp)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text("Search...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // filters stacked vertically instead of horizontal scroll
            FilterChip(
                selected = uiState.activeFilter == null && !uiState.favoritesOnly,
                onClick = { onFilterChange(null); onFavoritesToggle(false) },
                label = { Text("All") },
                modifier = Modifier.fillMaxWidth()
            )

            FilterChip(
                selected = uiState.favoritesOnly,
                onClick = { onFavoritesToggle(!uiState.favoritesOnly) },
                label = { Text("Favorites") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            GameStatus.entries.forEach { status ->
                FilterChip(
                    selected = uiState.activeFilter == status,
                    onClick = { onFilterChange(if (uiState.activeFilter == status) null else status) },
                    label = { Text(status.name.replace("_", " ")) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // divider between sidebar and list
        VerticalDivider()

        // right side — game list
        Box(modifier = Modifier.weight(1f)) {
            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                filteredGames.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No games found.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(filteredGames) { userGame ->
                            GameCard(
                                gameName = userGame.name,
                                coverUrl = userGame.coverUrl,
                                status = userGame.status,
                                isFavorite = userGame.isFavorite,
                                score = userGame.score,
                                onClick = { onGameClick(userGame.gameId) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterRow(
    activeFilter: GameStatus?,
    favoritesOnly: Boolean,
    onFilterChange: (GameStatus?) -> Unit,
    onFavoritesToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // all games chip
        FilterChip(
            selected = activeFilter == null && !favoritesOnly,
            onClick = { onFilterChange(null); onFavoritesToggle(false) },
            label = { Text("All") }
        )

        // favorites chip
        FilterChip(
            selected = favoritesOnly,
            onClick = { onFavoritesToggle(!favoritesOnly) },
            label = { Text("Favorites") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        )

        // status chips
        GameStatus.entries.forEach { status ->
            FilterChip(
                selected = activeFilter == status,
                onClick = { onFilterChange(if (activeFilter == status) null else status) },
                label = { Text(status.name.replace("_", " ")) }
            )
        }
    }
}