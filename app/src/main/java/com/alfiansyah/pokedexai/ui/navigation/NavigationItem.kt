package com.alfiansyah.pokedexai.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.alfiansyah.pokedexai.ui.navigation.screen.Screen

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val screen: Screen
)
