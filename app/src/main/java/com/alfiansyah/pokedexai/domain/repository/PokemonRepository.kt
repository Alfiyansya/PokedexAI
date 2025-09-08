package com.alfiansyah.pokedexai.domain.repository

import com.alfiansyah.pokedexai.data.source.local.FavoritePokemon
import com.alfiansyah.pokedexai.data.source.remote.responses.Pokemon
import com.alfiansyah.pokedexai.data.source.remote.responses.PokemonList
import com.alfiansyah.pokedexai.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): Flow<Resource<PokemonList>>
    suspend fun getPokemonInfo(pokemonName:String) : Flow<Resource<Pokemon>>

    fun isFavorite(pokemon: String): Flow<Boolean>
    suspend fun addToFavorite(pokemon: FavoritePokemon)
    suspend fun removeFromFavorite(pokemon: FavoritePokemon)

    fun getAllFavorites(): Flow<List<FavoritePokemon>>
}