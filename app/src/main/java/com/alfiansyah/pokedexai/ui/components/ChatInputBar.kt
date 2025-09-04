package com.alfiansyah.pokedexai.ui.components

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alfiansyah.pokedexai.ui.theme.PokedexAITheme

@Composable
fun ChatInputBar(
    prompt: String,
    selectedBitmap: Bitmap?,
    onPromptChange: (String) -> Unit,
    onImagePickerClick: () -> Unit,
    onClearImageClick: () -> Unit,
    onSendClick: () -> Unit
) {
    Surface(
        shadowElevation = 8.dp
    ) {
        Column {
            AnimatedVisibility(visible = selectedBitmap != null) {
                if (selectedBitmap != null) {
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        Image(
                            modifier = Modifier
                                .height(80.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            bitmap = selectedBitmap.asImageBitmap(),
                            contentDescription = "Selected Image",
                            contentScale = ContentScale.Fit
                        )
                        IconButton(
                            onClick = onClearImageClick,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                .size(24.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Clear Image", tint = Color.White)
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onImagePickerClick) {
                    Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Add Photo")
                }

                OutlinedTextField(
                    value = prompt,
                    onValueChange = onPromptChange,
                    placeholder = { Text("Ask Professor Gemini...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onSendClick,
                    enabled = prompt.isNotBlank() || selectedBitmap != null,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ChatInputBarPreview() {
    PokedexAITheme {
        ChatInputBar(
            prompt = "",
            selectedBitmap = null,
            onPromptChange = {},
            onImagePickerClick = {},
            onClearImageClick = {},
            onSendClick = {}
        )
    }
}