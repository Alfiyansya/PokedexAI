package com.alfiansyah.pokedexai.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.alfiansyah.pokedexai.data.models.PokemonListEntry
import com.alfiansyah.pokedexai.ui.navigation.screen.Screen
import com.alfiansyah.pokedexai.ui.pokemon_list.PokemonListViewModel
import com.alfiansyah.pokedexai.ui.theme.PokedexAITheme

@Composable
fun PokemonCard(
    entry: PokemonListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val defaultDominantColor = MaterialTheme.colorScheme.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(
        modifier = modifier
            .aspectRatio(0.8f) // Memberi rasio aspek pada keseluruhan Box
            .clickable {
                navController.navigate(
                    Screen.Detail.createRoute(dominantColor.toArgb(), entry.pokemonName)
                )
            }
    ) {
        // Surface untuk label nama
        Surface(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(1f), // Pastikan label nama berada di atas Card
            shape = RoundedCornerShape(50),
            color = Color(0xFFFBC02D), // Warna kuning khas Pokemon
            border = BorderStroke(2.dp, Color(0xFF424242))
        ) {
            Text(
                text = entry.pokemonName,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            )
        }

        // Card utama
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp), // Beri jarak dari atas agar label nama terlihat
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            border = BorderStroke(2.dp, Color(0xFF424242)),
        ) {
            // Box dengan background gradien
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                dominantColor,
                                defaultDominantColor
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // SATU-SATUNYA SubcomposeAsyncImage
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(entry.imageUrl)
                        .crossfade(true)
                        .listener(
                            onSuccess = { _, result ->
                                // Ekstrak warna dominan saat gambar berhasil dimuat
                                viewModel.calcDominantColor(result.drawable) { color ->
                                    dominantColor = color
                                }
                            }
                        )
                        .build(),
                    contentDescription = entry.pokemonName,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 24.dp, bottom = 8.dp, start = 8.dp, end = 8.dp),
                    contentScale = ContentScale.Fit,
                    loading = {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.scale(0.5f)
                        )
                    }
                )
            }
        }
    }
}


// --- Preview Functions (Tidak perlu diubah) ---

@Preview(showBackground = true, name = "PokemonCard New")
@Composable
fun PokemonCardPreview() {
    val dummyEntry = PokemonListEntry(
        pokemonName = "Pikachu",
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png",
        number = 25
    )
    PokedexAITheme {
        Box(
            modifier = Modifier
                .size(220.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            PokemonCard(
                entry = dummyEntry,
                navController = rememberNavController()
            )
        }
    }
}

@Preview(showBackground = true, name = "PokemonCard Grid Preview")
@Composable
fun PokemonCardGridPreview() {
    val dummyEntries = listOf(
        PokemonListEntry(
            "Pikachu",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png",
            25
        ),
        PokemonListEntry(
            "Charizard",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/6.png",
            6
        ),
        PokemonListEntry(
            "Blastoise",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/9.png",
            9
        ),
        PokemonListEntry(
            "Venusaur",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/3.png",
            3
        )
    )

    PokedexAITheme {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(8.dp),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp), // Beri jarak vertikal antar card
            horizontalArrangement = Arrangement.spacedBy(16.dp) // Beri jarak horizontal antar card
        ) {
            items(dummyEntries) { entry ->
                PokemonCard(
                    entry = entry,
                    navController = rememberNavController()
                )
            }
        }
    }
}