package com.alfiansyah.pokedexai.ui.gemini_chat

import android.graphics.Bitmap
import com.alfiansyah.pokedexai.data.models.chat.ChatItem

data class ChatState(
    val chatItemList: List<ChatItem> = emptyList(),
    val prompt: String = "",
    val selectedBitmap: Bitmap? = null,
    val isGeneratingResponse: Boolean = false,
    val errorMessage: String? = null
)
