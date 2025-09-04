package com.alfiansyah.pokedexai.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TypingIndicator(
){

    val dots = listOf(
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) },
    )

    LaunchedEffect(Unit ){
        dots.forEachIndexed { index,animatable ->
            launch {
                delay(index * 150L)
                while (true){
                    animatable.animateTo(1f, animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing))
                    animatable.animateTo(0f, animationSpec = tween(durationMillis = 300, easing = FastOutLinearInEasing))
                    delay(200L)
                }
            }
        }
    }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start
    ){
        Row(
            modifier = Modifier
                .widthIn(max = 100.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ){
            dots.forEach { animatable ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .offset(y = -animatable.value.dp * 6)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(0.7f))
                )
            }
        }
    }

}