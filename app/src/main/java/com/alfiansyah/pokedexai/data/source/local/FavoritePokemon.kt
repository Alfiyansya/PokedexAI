package com.alfiansyah.pokedexai.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_pokemon")
data class FavoritePokemon(
    @PrimaryKey
    val pokemonName: String,
    val pokemonImageUrl: String,
    val number: Int
)