package com.alfiansyah.pokedexai.ui.pokemon_list

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.alfiansyah.pokedexai.R
import com.alfiansyah.pokedexai.data.models.PokemonListEntry
import com.alfiansyah.pokedexai.ui.components.PokemonCard
import com.alfiansyah.pokedexai.ui.components.SearchBar
import com.alfiansyah.pokedexai.ui.theme.PokedexAITheme

@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
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
                    hint = stringResource(R.string.search_pokemon_name),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                ) {
                    viewModel.searchPokemonList(it)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            PokemonList(navController = navController)
        }
    }
}

@Composable
fun PokemonList(navController: NavController, viewModel: PokemonListViewModel = hiltViewModel()) {
    val pokemonList by viewModel.pokemonList.collectAsStateWithLifecycle()
    val endReached by viewModel.endReached.collectAsStateWithLifecycle()
    val loadError by viewModel.loadError.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isSearching by remember { viewModel.isSearching }

    LazyColumn(
        contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp),
    ) {
        val itemCount = if (pokemonList.size % 2 == 0) {
            pokemonList.size / 2
        } else {
            pokemonList.size / 2 + 1
        }
        items(itemCount) {
            if (it >= itemCount - 1 && !endReached && !isLoading && !isSearching) {
                viewModel.loadPokemonPaginated()
            }
            PokedexRow(rowIndex = it, entries = pokemonList, navController = navController)
        }
    }
    Box(
        contentAlignment = Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)

        }
        if (loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.loadPokemonPaginated()
            }
        }
    }

}

@Composable
fun PokedexRow(
    rowIndex: Int, entries: List<PokemonListEntry>,
    navController: NavController
) {
    Column {
        Row {
            PokemonCard(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (entries.size >= rowIndex * 2 + 2) {
                PokemonCard(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f),
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

}

@Composable
fun RetrySection(error: String, onRetry: () -> Unit) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }

}

@Preview(showBackground = true, name = "PokemonListScreen")
@Composable
fun PokemonListScreenPreview() {
    PokedexAITheme {
        PokemonListScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true, name = "PokedexCard")
@Composable
fun PokedexCardPreview() {
    val dummyEntry = PokemonListEntry(
        pokemonName = "Pikachu",
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png",
        number = 25
    )

    PokedexAITheme {
        Box(modifier = Modifier.padding(16.dp)) {
            PokemonCard(
                entry = dummyEntry,
                navController = rememberNavController(),
                modifier = Modifier.width(200.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "PokedexRow")
@Composable
fun PokedexRowPreview() {
    val dummyEntries = listOf(
        PokemonListEntry(
            pokemonName = "Pikachu",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png",
            number = 25
        ),
        PokemonListEntry(
            pokemonName = "Charmander",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/4.png",
            number = 4
        )
    )

    PokedexAITheme {
        PokedexRow(rowIndex = 0, entries = dummyEntries, navController = rememberNavController())
    }
}

@Preview(showBackground = true, name = "RetrySection")
@Composable
fun RetrySectionPreview() {
    PokedexAITheme {
        RetrySection(error = "Failed to load data") {
            // onRetry action
        }
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
fun LoadingStatePreview() {
    PokedexAITheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun rememberGifImageLoader(): ImageLoader {
    val context = LocalContext.current
    return remember {
        ImageLoader.Builder(context).components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.build()
    }
}