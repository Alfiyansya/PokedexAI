package com.alfiansyah.pokedexai.di

import com.alfiansyah.pokedexai.data.repository.PokemonRepositoryImpl
import com.alfiansyah.pokedexai.data.source.local.FavoritePokemonDao
import com.alfiansyah.pokedexai.data.source.remote.PokeApi
import com.alfiansyah.pokedexai.domain.repository.PokemonRepository
import com.alfiansyah.pokedexai.util.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePokemonRepository(
        api: PokeApi,
        favoritePokemonDao: FavoritePokemonDao
    ) : PokemonRepository = PokemonRepositoryImpl(api, favoritePokemonDao)

    @Singleton
    @Provides
    fun providePokeApi(): PokeApi{
        val intercepter = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().apply {
            this.addInterceptor(intercepter)
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .writeTimeout(25,TimeUnit.SECONDS)

        }.build()
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(PokeApi::class.java)

    }
}