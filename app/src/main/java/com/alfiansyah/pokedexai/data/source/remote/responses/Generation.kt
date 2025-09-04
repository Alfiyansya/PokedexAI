package com.alfiansyah.pokedexai.data.source.remote.responses


import com.google.gson.annotations.SerializedName

data class Generation(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)