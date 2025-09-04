package com.alfiansyah.pokedexai.data.source.remote.responses.species


import com.google.gson.annotations.SerializedName

data class Genera(
    @SerializedName("genus")
    val genus: String,
    @SerializedName("language")
    val language: Language
)