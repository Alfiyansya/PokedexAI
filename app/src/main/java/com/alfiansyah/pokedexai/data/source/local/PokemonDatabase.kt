package com.alfiansyah.pokedexai.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoritePokemon::class],
    version = 1,
    exportSchema = false
)
abstract class PokemonDatabase: RoomDatabase(){
    abstract fun favoritePokemonDao(): FavoritePokemonDao
}