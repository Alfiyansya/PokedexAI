package com.alfiansyah.pokedexai.ui.pokemon_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfiansyah.pokedexai.data.source.local.FavoritePokemon
import com.alfiansyah.pokedexai.data.source.remote.responses.Pokemon
import com.alfiansyah.pokedexai.domain.repository.PokemonRepository
import com.alfiansyah.pokedexai.ui.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(private val repository: PokemonRepository) :
    ViewModel() {
    private val _pokemonInfo = MutableStateFlow<Resource<Pokemon>>(Resource.Loading())
    val pokemonInfo: StateFlow<Resource<Pokemon>> = _pokemonInfo

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite
    fun getPokemonInfo(pokemonName: String) {
        viewModelScope.launch {
            launch {
                repository.isFavorite(pokemonName).collect { isFavorite ->
                    _isFavorite.value = isFavorite
                }
            }
            repository.getPokemonInfo(pokemonName).collect { pokemonInfo ->
                _pokemonInfo.value = pokemonInfo
            }
        }
    }

    fun toggleFavoriteStatus() {
        viewModelScope.launch {
            val pokemon = (_pokemonInfo.value as Resource.Success).data
            if (pokemon != null) {
                val favoritePokemon = FavoritePokemon(
                    pokemonName = pokemon.name,
                    number = pokemon.id,
                    pokemonImageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png"
                )
                if (_isFavorite.value) {
                    repository.removeFromFavorite(favoritePokemon)
                } else {
                    repository.addToFavorite(favoritePokemon)
                }
            }
        }
    }
}