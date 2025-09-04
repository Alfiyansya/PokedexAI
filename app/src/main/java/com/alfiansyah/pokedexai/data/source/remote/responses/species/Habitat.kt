package com.alfiansyah.pokedexai.data.source.remote.responses.species


import com.google.gson.annotations.SerializedName

data class Habitat(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)