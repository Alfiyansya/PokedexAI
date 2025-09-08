package com.alfiansyah.pokedexai.ui.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PikachuYellow = Color(0xFFFBC02D)
val WaterBlue = Color(0xFF1E88E5)

val LightGray = Color(0xFFF5F5F5)
val DarkCharcoal = Color(0xFF121212)
val MediumGray = Color(0xFF212121)

private val DarkColorPalette = darkColorScheme(
    primary = PikachuYellow,
    onPrimary = Color.Black,
    secondary = WaterBlue,
    onSecondary = Color.White,
    tertiary = WaterBlue,
    onTertiary = Color.White,
    background = DarkCharcoal,
    onBackground = Color.White,
    surface = MediumGray,
    onSurface = Color.White,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

private val LightColorPalette = lightColorScheme(
    primary = WaterBlue,
    onPrimary = Color.White,
    secondary = WaterBlue,
    onSecondary = Color.White,
    tertiary = PikachuYellow,
    onTertiary = Color.Black,
    background = LightGray,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    error = Color(0xFFB00020),
    onError = Color.White
)
@Composable
fun PokedexAITheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}