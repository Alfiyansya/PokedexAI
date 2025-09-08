package com.alfiansyah.pokedexai.util

import android.graphics.Bitmap
import com.alfiansyah.pokedexai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ChatData {
    private val generativeModel by lazy {
        GenerativeModel(
            modelName = "gemini-1.5-pro", // Gemini 1.5 Pro lebih powerful dan lebih baru
            apiKey = BuildConfig.API_KEY,
            systemInstruction = content {
                text("""
                You are a Pokémon expert and enthusiast named Professor Gemini.
                Your sole purpose is to discuss the world of Pokémon. This includes Pokémon species,
                game mechanics, items, locations, characters, and lore from the games, anime, and manga.
                You must ONLY answer questions related to Pokémon.
                If the user asks about anything outside of the Pokémon universe, you must politely
                decline and gently steer the conversation back to Pokémon.
                Do not answer questions about real-world topics, other franchises, or personal opinions.
                Keep your responses friendly, engaging, and informative, as if you are a real Pokémon Professor.
            """.trimIndent())
            }
        )
    }
    suspend fun getResponse(prompt: String): String{
        return withContext(Dispatchers.IO){
            try {
                val response = generativeModel.generateContent(prompt)
                response.text ?: "Error: No Text response received"
            }catch (e : Exception){
                throw e
            }
        }

    }


    suspend fun getResponseWithImage(prompt: String, bitmap: Bitmap): String {
        return withContext(Dispatchers.IO) {
            try {
                val inputContent = content {
                    image(bitmap)
                    text(prompt)
                }
                // Gunakan instance yang sudah ada
                val response = generativeModel.generateContent(inputContent)
                response.text ?: "Error: No text response received."
            } catch (e: Exception) {
                // Melemparkan exception ke ViewModel
                throw e
            }
        }
    }
}