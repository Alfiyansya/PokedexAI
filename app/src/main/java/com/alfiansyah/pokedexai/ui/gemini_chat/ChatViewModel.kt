package com.alfiansyah.pokedexai.ui.gemini_chat

import android.app.Application
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfiansyah.pokedexai.data.models.chat.ChatItem
import com.alfiansyah.pokedexai.ui.util.ChatData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val application: Application // Inject Application context
) : ViewModel() {
    private val _chatState = MutableStateFlow(ChatState())
    val chatState = _chatState.asStateFlow()

    init {
        getGreeting()
    }

    private fun getGreeting() {
        viewModelScope.launch {
            _chatState.update { it.copy(isGeneratingResponse = true, errorMessage = null) }
            try {
                val greetingPrompt = "Give an opening greeting as Professor Gemini. Welcome me to the world of Pokémon."
                val responseText = ChatData.getResponse(greetingPrompt)
                _chatState.update {
                    it.copy(
                        // Gunakan cara yang konsisten
                        chatItemList = listOf(ChatItem(responseText, null, false)) + it.chatItemList,
                        isGeneratingResponse = false
                    )
                }
            } catch (e: Exception) {
                _chatState.update {
                    it.copy(
                        isGeneratingResponse = false,
                        errorMessage = e.message ?: "An error occurred"
                    )
                }
            }
        }
    }

    fun updatePrompt(newPrompt: String){
        _chatState.update { it.copy(prompt = newPrompt)}
    }
    fun sendPrompt(){
        val prompt = _chatState.value.prompt
        val bitmap = _chatState.value.selectedBitmap

        if (prompt.isNotEmpty() || bitmap != null) {
            addPrompt(prompt = prompt, bitmap = bitmap)
            if (bitmap != null) {
                getResponseWithImage(prompt = prompt, bitmap = bitmap)
            }else{
                getResponse(prompt = prompt)
            }
        }
    }
    fun onImageSelected(uri: Uri){
        viewModelScope.launch {
            try {
                val bitmap = withContext(Dispatchers.IO){
                    uriToBitmap(uri)
                }
                _chatState.update { it.copy(selectedBitmap = bitmap) }
            }catch (e: Exception){
                Timber.tag("ChatViewModel").e("Failed to load image: ${e.message}")
                _chatState.update { it.copy(errorMessage = e.message ?: "Failed to load image") }
            }
        }
    }

    fun clearSelectedImage(){
        _chatState.update { it.copy(selectedBitmap = null) }
    }

    private fun addPrompt(prompt: String, bitmap: Bitmap?) {
        _chatState.update {
            val newChatItem = ChatItem(prompt, bitmap, true)
            it.copy(
                chatItemList = listOf(newChatItem) + it.chatItemList,
                prompt = "",
                selectedBitmap = null
            )
        }
    }

    private fun getResponse(prompt: String) {
        viewModelScope.launch {
            // Aktifkan indikator loading
            _chatState.update { it.copy(isGeneratingResponse = true, errorMessage = null) }
            try {
                val responseText = ChatData.getResponse(prompt)
                _chatState.update {
                    val newChatItem = ChatItem(responseText, null, false)
                    it.copy(
                        chatItemList = listOf(newChatItem) + it.chatItemList,
                        isGeneratingResponse = false
                    )
                }
            } catch (e: Exception) {
                _chatState.update {
                    it.copy(
                        isGeneratingResponse = false,
                        errorMessage = e.message ?: "An unknown error occurred."
                    )
                }
            }
        }
    }

    private fun getResponseWithImage(prompt: String, bitmap: Bitmap) {
        viewModelScope.launch {
            // Aktifkan indikator loading
            _chatState.update { it.copy(isGeneratingResponse = true, errorMessage = null) }
            try {
                val responseText = ChatData.getResponseWithImage(prompt, bitmap)
                _chatState.update {
                    val newChatItem = ChatItem(responseText, null, false)
                    it.copy(
                        chatItemList = listOf(newChatItem) + it.chatItemList,
                        isGeneratingResponse = false
                    )
                }
            } catch (e: Exception) {
                _chatState.update {
                    it.copy(
                        isGeneratingResponse = false,
                        errorMessage = e.message ?: "An unknown error occurred."
                    )
                }
            }
        }
    }
    // Fungsi utilitas untuk konversi Uri ke Bitmap di background thread
    private suspend fun uriToBitmap(uri: Uri): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val contentResolver = application.contentResolver
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(contentResolver, uri)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _chatState.update { it.copy(errorMessage = "Failed to load image.") }
                null
            }
        }
    }
}