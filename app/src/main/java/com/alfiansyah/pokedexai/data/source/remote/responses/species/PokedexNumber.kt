package com.alfiansyah.pokedexai.data.source.remote.responses.species


import com.google.gson.annotations.SerializedName

data class PokedexNumber(
    @SerializedName("entry_number")
    val entryNumber: Int,
    @SerializedName("pokedex")
    val pokedex: Pokedex
)