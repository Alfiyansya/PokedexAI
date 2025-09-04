package com.alfiansyah.pokedexai.ui.pokemon_favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.alfiansyah.pokedexai.R
import com.alfiansyah.pokedexai.data.models.PokemonListEntry
import com.alfiansyah.pokedexai.ui.components.SearchBar
import com.alfiansyah.pokedexai.ui.pokemon_list.PokedexRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonFavoriteListScreen(
    navController: NavController,
    viewModel: PokemonFavoriteListViewModel = hiltViewModel()
) {
    val favoriteList by viewModel.favoritePokemonList.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            Column(modifier = Modifier) {
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = painterResource(R.drawable.ic_international_pokemon_logo),
                    contentDescription = "Pokemon Logo",
                    modifier = Modifier.fillMaxWidth()
                        .align(CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                SearchBar(
                    hint = stringResource(R.string.search_favorite_pokemon),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                    onSearch = {
                        viewModel.onSearchQueryChanged(it)
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (favoriteList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.no_favorite_pokemon),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                val pokedexEntries = favoriteList.map { favorite ->
                    PokemonListEntry(
                        pokemonName = favorite.pokemonName,
                        imageUrl = favorite.pokemonImageUrl,
                        number = favorite.number
                    )
                }
                LazyColumn(
                    contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp),
                    modifier = Modifier
                ) {
                    val itemCount = if (pokedexEntries.size % 2 == 0) {
                        pokedexEntries.size / 2
                    } else {
                        pokedexEntries.size / 2 + 1
                    }
                    items(itemCount) {
                        PokedexRow(
                            rowIndex = it,
                            entries = pokedexEntries,
                            navController = navController
                        )
                    }
                }

            }
        }
    }

}