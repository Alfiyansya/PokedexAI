package com.alfiansyah.pokedexai.data.source.remote.responses.species


import com.google.gson.annotations.SerializedName

data class EvolutionChain(
    @SerializedName("url")
    val url: String
)