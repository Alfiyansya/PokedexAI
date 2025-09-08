package com.alfiansyah.pokedexai.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.alfiansyah.pokedexai.R
import com.alfiansyah.pokedexai.ui.components.AppFab
import com.alfiansyah.pokedexai.ui.gemini_chat.GeminiChatScreen
import com.alfiansyah.pokedexai.ui.navigation.NavigationItem
import com.alfiansyah.pokedexai.ui.navigation.screen.Screen
import com.alfiansyah.pokedexai.ui.pokemon_detail.PokemonDetailScreen
import com.alfiansyah.pokedexai.ui.pokemon_favorite.PokemonFavoriteListScreen
import com.alfiansyah.pokedexai.ui.pokemon_list.PokemonListScreen
import com.alfiansyah.pokedexai.ui.pokemon_list.rememberGifImageLoader
import java.util.Locale

@Composable
fun PokedexAIApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val shouldShowBottomBar = currentRoute in listOf(Screen.Home.route, Screen.Favorite.route)

    val onNavigate: (String) -> Unit = { route ->
        navController.navigate(route){
            popUpTo(navController.graph.findStartDestination().id){
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                CustomBottomNavigationBar(
                    currentRoute = currentRoute ?: Screen.Home.route,
                    onNavigate = onNavigate
                )
            }
        },
        floatingActionButton = {
            if (shouldShowBottomBar) {
                val gifLoader = rememberGifImageLoader()
                AppFab(
                    onClick = { navController.navigate(Screen.GeminiChat.route) },
                    modifier = Modifier
                        .offset(y = 60.dp)
                        .size(72.dp)
                    ,
                    shape = CircleShape,
                    containerColor = Color.Unspecified,
                    ){
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(R.drawable.gemini_2025_animated)
                            .build(),
                        imageLoader = gifLoader,
                        contentDescription = "Chat with Gemini",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

            }
        },
        // 2. Tentukan posisi FAB di tengah. Scaffold akan secara otomatis
        //    menempatkannya di atas BottomAppBar/NavigationBar.
        floatingActionButtonPosition = FabPosition.Center,
        modifier = modifier.safeContentPadding()
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                PokemonListScreen(navController = navController)
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(
                    navArgument("dominantColor") {
                        type = NavType.IntType
                    },
                    navArgument("pokemonName") {
                        type = NavType.StringType
                    })
            )
            {
                val dominantColor = remember {
                    val color = it.arguments?.getInt("dominantColor")
                    color?.let { Color(it) } ?: Color.White
                }
                val pokemonName = remember {
                    it.arguments?.getString("pokemonName")
                }

                PokemonDetailScreen(
                    dominantColor = dominantColor,
                    pokemonName = pokemonName?.lowercase(Locale.ROOT) ?: "",
                    navController = navController
                )
            }
            composable(Screen.GeminiChat.route) {
                GeminiChatScreen(
                    navController = navController
                )
            }
            composable(Screen.Favorite.route) {
                PokemonFavoriteListScreen(
                    navController = navController
                )
            }
        }

    }


}

@Composable
fun CustomBottomNavigationBar(
    currentRoute: String,
    onNavigate : (String)-> Unit
) {
    val navigationItems = listOf(
        NavigationItem(
            title = stringResource(R.string.home),
            icon = Icons.Default.Home,
            screen = Screen.Home
        ),
        NavigationItem(
            title = stringResource(R.string.favorite),
            icon = Icons.Default.Favorite,
            screen = Screen.Favorite,
        )
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(80.dp)
            .paint(
                painter = painterResource(R.drawable.bottom_navigation),
                contentScale = ContentScale.FillHeight
            )
    ) {
        navigationItems.forEach { item ->
            val isSelected = currentRoute == item.screen.route
            CustomBottomBarItem(
                item = item,
                isSelected = isSelected,
                onClick = {
                    onNavigate(item.screen.route)
                }
            )
        }
    }
}

@Composable
fun RowScope.CustomBottomBarItem(
    item: NavigationItem,
    isSelected: Boolean,
    onClick: () -> Unit
){
    val contentColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick)
    ){
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = contentColor,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.title,
            color = contentColor
        )
    }

}
