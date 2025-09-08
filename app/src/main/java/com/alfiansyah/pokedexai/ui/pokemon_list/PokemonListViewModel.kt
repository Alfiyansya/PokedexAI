package com.alfiansyah.pokedexai.ui.pokemon_list

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.alfiansyah.pokedexai.data.models.PokemonListEntry
import com.alfiansyah.pokedexai.domain.repository.PokemonRepository
import com.alfiansyah.pokedexai.util.Constant.PAGE_SIZE
import com.alfiansyah.pokedexai.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var curPage = 0

    private val _pokemonList = MutableStateFlow<List<PokemonListEntry>>(emptyList())
    val pokemonList: StateFlow<List<PokemonListEntry>> = _pokemonList

    private val _loadError = MutableStateFlow<String>("")
    val loadError: StateFlow<String> = _loadError

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading : StateFlow<Boolean> = _isLoading

    private val _endReached = MutableStateFlow<Boolean>(false)
    val endReached : StateFlow<Boolean> = _endReached

    private var cachedPokemonList = listOf<PokemonListEntry>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    init {
        loadPokemonPaginated()
    }

    fun searchPokemonList(query: String) {
        val listToSearch = if(isSearchStarting) {
            _pokemonList.value
        } else {
            cachedPokemonList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if(query.isEmpty()) {
                _pokemonList.value = cachedPokemonList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.pokemonName.contains(query.trim(), ignoreCase = true) ||
                        it.number.toString() == query.trim()
            }
            if(isSearchStarting) {
                cachedPokemonList = pokemonList.value
                isSearchStarting = false
            }
            _pokemonList.value = results
            isSearching.value = true
        }
    }

    fun loadPokemonPaginated() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getPokemonList(PAGE_SIZE, curPage * PAGE_SIZE).collect {result ->
                when(result) {

                    is Resource.Success -> {
                        Timber.tag("datanya").i(result.data?.results.toString())
                        _endReached.value = curPage * PAGE_SIZE >= result.data!!.count
                        val pokedexEntries = result.data.results.mapIndexed { index, entry ->
                            val number = if(entry.url.endsWith("/")) {
                                entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                            } else {
                                entry.url.takeLastWhile { it.isDigit() }
                            }
                            val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${number}.png"
                            PokemonListEntry(entry.name.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                            }, url, number.toInt())
                        }
                        curPage++

                        _loadError.value = ""
                        _isLoading.value = false
                        _pokemonList.value += pokedexEntries
                    }
                    is Resource.Error -> {
                        _loadError.value = result.message!!
                        _isLoading.value = false
                    }

                    is Resource.Loading -> {
                        _isLoading.value = true
                    }
                }

            }

        }
    }

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}