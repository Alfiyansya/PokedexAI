package com.alfiansyah.pokedexai.ui.gemini_chat

import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.alfiansyah.pokedexai.data.models.chat.ChatItem
import com.alfiansyah.pokedexai.ui.components.ChatInputBar
import com.alfiansyah.pokedexai.ui.components.TypingIndicator
import com.alfiansyah.pokedexai.ui.theme.PokedexAITheme

@Composable
fun GeminiChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val chatState by viewModel.chatState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let { viewModel.onImageSelected(it) }
        }
    )

    LaunchedEffect(chatState.errorMessage) {
        chatState.errorMessage?.let {
            Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
        }
    }

    ChatScreenContent(
        navController = navController,
        chatState = chatState,
        onUpdatePrompt = viewModel::updatePrompt,
        onSendClick = viewModel::sendPrompt,
        onImagePickerClick = {
            imagePickerLauncher.launch(
                PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    .build()
            )
        },
        onClearImageClick = viewModel::clearSelectedImage
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatScreenContent(
    navController: NavController?,
    chatState: ChatState,
    onUpdatePrompt: (String) -> Unit,
    onSendClick: () -> Unit,
    onImagePickerClick: () -> Unit,
    onClearImageClick: () -> Unit
) {
    val lazyListState = rememberLazyListState()

    // Scroll ke item terbaru (paling bawah) saat list berubah
    LaunchedEffect(chatState.chatItemList.size) {
        if (chatState.chatItemList.isNotEmpty()) {
            lazyListState.animateScrollToItem(index = 0)
        }
    }

    Scaffold(
        topBar = {
            TopBarContent(
                title = "Professor Gemini",
                onBackClicked = { navController?.popBackStack() }
            )
        },
        bottomBar = {
            ChatInputBar(
                prompt = chatState.prompt,
                selectedBitmap = chatState.selectedBitmap,
                onPromptChange = onUpdatePrompt,
                onImagePickerClick = onImagePickerClick,
                onClearImageClick = onClearImageClick,
                onSendClick = onSendClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding(),
            state = lazyListState,
            reverseLayout = true,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (chatState.isGeneratingResponse) {
                item(key = "typing_indicator") {
                    Box(modifier = Modifier.animateItem(tween(durationMillis = 350))) {
                        TypingIndicator()
                    }
                }
            }
            items(
                items = chatState.chatItemList,
                key = { it.hashCode() }
            ) { chat ->
                Box(modifier = Modifier.animateItem(tween(durationMillis = 350))) {
                    if (chat.isFromUser) {
                        UserChatItem(prompt = chat.prompt, bitmap = chat.bitmap)
                    } else {
                        ModelChatItem(response = chat.prompt)
                    }
                }
            }
        }
    }
}

@Composable
fun UserChatItem(prompt: String, bitmap: Bitmap?) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        bitmap?.let {
            Image(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .padding(bottom = 4.dp),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                bitmap = it.asImageBitmap()
            )
        }
        if (prompt.isNotBlank()) {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(16.dp, 16.dp, 4.dp, 16.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = prompt,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun ModelChatItem(response: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp),
            text = response,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarContent(title: String, onBackClicked: () -> Unit) {
    TopAppBar(
        title = { Text(text = title, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

private val dummyChatState = ChatState(
    chatItemList = listOf(


        ChatItem(
            "Bulbasaur is a Grass/Poison-type Pokémon. It is known for the plant bulb on its back, which grows as it ages. It evolves into Ivysaur.",
            null,
            false
        ),

        ChatItem(
            "What is a Bulbasaur?",
            null,
            true
        ),
        ChatItem(
            "Hello! I am Professor Gemini, your guide to the world of Pokémon. Ask me anything about Pokémon and I'll be happy to help!",
            null,
            false
        ),
    )
)

@Preview(showBackground = true)
@Composable
fun GeminiChatScreenPreview() {
    PokedexAITheme {
        ChatScreenContent(
            navController = null,
            chatState = dummyChatState,
            onUpdatePrompt = {},
            onSendClick = {},
            onImagePickerClick = {},
            onClearImageClick = {}
        )
    }
}