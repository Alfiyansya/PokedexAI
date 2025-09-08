package com.alfiansyah.pokedexai.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alfiansyah.pokedexai.ui.theme.PokedexAITheme

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onSearch(it)
        },
        modifier = modifier.fillMaxWidth()
            .border(width = 1.dp, color = Color.Black, shape = CircleShape),
        placeholder = {
            Text( text = hint, color = Color.Gray)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.Gray
            )
        },
        trailingIcon = {
            if (text.isNotEmpty()) {
                IconButton(onClick = {
                    text = ""
                    onSearch("")
                }){
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear Search Icon",
                        tint = Color(0xFFE53935)
                    )
                }
            }
        },
        singleLine = true,
        shape = CircleShape,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.LightGray,
            // Warna border saat TextField difokuskan (diklik).
            focusedIndicatorColor = Color.Black, // Merah khas Poké Ball
            // Warna border saat tidak fokus.
            unfocusedIndicatorColor = Color.LightGray,
            // Warna kursor.
            cursorColor = Color(0xFFE53935) // Merah
        )
    )

}
@Preview(name = "SearchBar Themed", showBackground = true)
@Composable
fun SearchBarThemedPreview() {
    PokedexAITheme {
        SearchBar(
            modifier = Modifier.padding(16.dp),
            hint = "Search Pokémon..."
        )
    }
}