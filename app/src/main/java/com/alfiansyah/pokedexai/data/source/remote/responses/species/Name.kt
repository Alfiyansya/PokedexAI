package com.alfiansyah.pokedexai.data.source.remote.responses.species


import com.google.gson.annotations.SerializedName

data class Name(
    @SerializedName("language")
    val language: Language,
    @SerializedName("name")
    val name: String
)