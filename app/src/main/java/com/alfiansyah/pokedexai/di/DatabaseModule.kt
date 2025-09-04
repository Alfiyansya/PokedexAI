package com.alfiansyah.pokedexai.di

import android.content.Context
import androidx.room.Room
import com.alfiansyah.pokedexai.data.source.local.FavoritePokemonDao
import com.alfiansyah.pokedexai.data.source.local.PokemonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providePokedexDatabase(
        @ApplicationContext context: Context
    ): PokemonDatabase {
        return Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            "pokemon_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoritePokemonDao(database: PokemonDatabase): FavoritePokemonDao{
        return database.favoritePokemonDao()
    }
}