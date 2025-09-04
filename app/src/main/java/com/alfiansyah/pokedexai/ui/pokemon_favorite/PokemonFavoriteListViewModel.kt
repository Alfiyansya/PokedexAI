package com.alfiansyah.pokedexai.ui.pokemon_favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfiansyah.pokedexai.data.source.local.FavoritePokemon
import com.alfiansyah.pokedexai.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonFavoriteListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    private val _allFavorites = MutableStateFlow<List<FavoritePokemon>>(emptyList())
    val favoritePokemonList: StateFlow<List<FavoritePokemon>> =
        _searchQuery.combine(_allFavorites) { query, favorites ->
            if (query.isBlank()) {
                favorites
            } else {
                favorites.filter {
                    it.pokemonName.contains(query, ignoreCase = true)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000, 0),
            initialValue = emptyList()
        )


    init {
        getFavoritePokemon()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private fun getFavoritePokemon() {
        viewModelScope.launch {
            repository.getAllFavorites()
                .catch {

                }
                .collect { favorites ->
                    _allFavorites.value = favorites
                }
        }
    }
}