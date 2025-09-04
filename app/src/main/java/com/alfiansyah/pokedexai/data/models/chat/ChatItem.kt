package com.alfiansyah.pokedexai.data.models.chat

import android.graphics.Bitmap

data class ChatItem(
    val prompt: String,
    val bitmap: Bitmap?,
    val isFromUser: Boolean
)
