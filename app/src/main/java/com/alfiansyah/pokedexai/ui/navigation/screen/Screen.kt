package com.alfiansyah.pokedexai.ui.navigation.screen

sealed class Screen(val route: String) {
    data object Home: Screen("home")
    data object Favorite: Screen("favorite")
    data object Detail : Screen("detail/{dominantColor}/{pokemonName}"){
        fun createRoute(dominantColor: Int, pokemonName: String) = "detail/$dominantColor/$pokemonName"
    }
    data object GeminiChat: Screen("gemini_chat")
}