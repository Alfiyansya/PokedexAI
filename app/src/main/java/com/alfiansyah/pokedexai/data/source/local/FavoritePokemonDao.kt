package com.alfiansyah.pokedexai.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePokemonDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoritePokemon(pokemon: FavoritePokemon)

    @Delete
    suspend fun deleteFavoritePokemon(pokemon: FavoritePokemon)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_pokemon WHERE pokemonName= :pokemonName LIMIT 1)")
    fun isFavorite(pokemonName: String): Flow<Boolean>
    @Query("SELECT * FROM favorite_pokemon ORDER BY pokemonName ASC")
    fun getALlFavorites(): Flow<List<FavoritePokemon>>
}