package com.alfiansyah.pokedexai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alfiansyah.pokedexai.ui.theme.PokedexAITheme
import timber.log.Timber

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier){
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier.fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = !it.isFocused && text.isEmpty()
                    Timber.tag("FocusState").d(it.toString())
                    Timber.tag("FocusState1").d(it.isFocused.toString())
                    Timber.tag("FocusState2").d("${!it.isFocused && text.isEmpty()}")
                }
        )

        if (isHintDisplayed){
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp))
        }
    }

}
@Preview(name = "SearchBar with Hint", showBackground = true)
@Composable
fun SearchBarWithHintPreview() {
    PokedexAITheme {
        SearchBar(
            modifier = Modifier.padding(16.dp),
            hint = "Search Pokémon..."
        )
    }
}
@Preview(name = "SearchBar with Text", showBackground = true)
@Composable
fun SearchBarWithTextPreview() {
    PokedexAITheme {
        var text by remember { mutableStateOf("Pikachu") }

        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(5.dp, CircleShape)
                    .background(Color.White, CircleShape)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}