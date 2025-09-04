package com.alfiansyah.pokedexai.data.repository

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.alfiansyah.pokedexai.data.source.local.FavoritePokemon
import com.alfiansyah.pokedexai.data.source.local.FavoritePokemonDao
import com.alfiansyah.pokedexai.data.source.remote.PokeApi
import com.alfiansyah.pokedexai.data.source.remote.responses.Pokemon
import com.alfiansyah.pokedexai.data.source.remote.responses.PokemonList
import com.alfiansyah.pokedexai.domain.repository.PokemonRepository
import com.alfiansyah.pokedexai.ui.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val pokeApi: PokeApi,
    private val favoritePokemonDao: FavoritePokemonDao
): PokemonRepository{
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getPokemonList(limit: Int, offset: Int) : Flow<Resource<PokemonList>> =
        flow{
            try {
                emit(Resource.Success(pokeApi.getPokemonList(limit,offset)))
            }catch (e: HttpException){
                emit (Resource.Error("HTTP Error: ${e.message}"))
            }catch (_: IOException){
                emit(Resource.Error("Couldn't reach server. Check your internet connection"))
            }catch (_: Exception){
                emit(Resource.Error("An unknown error occured"))
            }
        }
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getPokemonInfo(pokemonName:String) : Flow<Resource<Pokemon>> = flow{
        try {
            emit(Resource.Success(pokeApi.getPokemonInfo(pokemonName)))
        }catch (e: HttpException){
            emit (Resource.Error("HTTP Error: ${e.message}"))
        }catch (_: IOException){
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }catch (_: Exception){
            emit(Resource.Error("An unknown error occured"))
        }
    }

    override fun isFavorite(pokemon: String): Flow<Boolean> {
        return favoritePokemonDao.isFavorite(pokemon)
    }

    override suspend fun addToFavorite(pokemon: FavoritePokemon) {
        return favoritePokemonDao.insertFavoritePokemon(pokemon)
    }

    override suspend fun removeFromFavorite(pokemon: FavoritePokemon) {
        return favoritePokemonDao.deleteFavoritePokemon(pokemon)
    }

    override fun getAllFavorites(): Flow<List<FavoritePokemon>> {
        return favoritePokemonDao.getALlFavorites()
    }
}