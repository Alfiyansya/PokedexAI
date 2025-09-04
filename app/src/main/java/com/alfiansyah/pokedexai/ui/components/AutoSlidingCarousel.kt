package com.alfiansyah.pokedexai.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alfiansyah.pokedexai.ui.theme.PokedexAITheme
import kotlinx.coroutines.delay

@Composable
fun AutoSlidingCarousel(
    modifier: Modifier = Modifier,
    itemsCount: Int,
    autoSlidingDuration: Long = 2_000,
    autoScrollAnimationMillis: Int = 450,
    selectedColor: Color = Color.White,
    unSelectedColor: Color = Color.LightGray,
    dotSize: Dp = 8.dp,
    indicatorContainerColor: Color = Color.Black.copy(alpha = 0.5f),
    carouselHeight: Dp = 180.dp,
    indicatorTopPadding: Dp = 32.dp,
    itemContent: @Composable (index: Int) -> Unit,
) {
    if (itemsCount <= 0) return

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { itemsCount }
    )

    AutoPagerHost(
        pagerState = pagerState,
        itemsCount = itemsCount,
        autoSlidingDuration = autoSlidingDuration,
        autoScrollAnimationMillis = autoScrollAnimationMillis,
        selectedColor = selectedColor,
        unSelectedColor = unSelectedColor,
        dotSize = dotSize,
        indicatorContainerColor = indicatorContainerColor,
        itemContent = itemContent,
        carouselHeight = carouselHeight,
        indicatorTopPadding = indicatorTopPadding,
        modifier = modifier
    )
}

@Composable
private fun AutoPagerHost(
    pagerState: PagerState,
    itemsCount: Int,
    autoSlidingDuration: Long,
    autoScrollAnimationMillis: Int,
    selectedColor: Color,
    unSelectedColor: Color,
    dotSize: Dp,
    indicatorContainerColor: Color,
    itemContent: @Composable (index: Int) -> Unit,
    carouselHeight: Dp,
    indicatorTopPadding: Dp,
    modifier: Modifier = Modifier
) {
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    LaunchedEffect(itemsCount, autoSlidingDuration) {
        while (true) {
            delay(autoSlidingDuration)
            val userInteracting = isDragged || pagerState.isScrollInProgress
            if (!userInteracting) {
                val next = (pagerState.currentPage + 1) % itemsCount
                pagerState.animateScrollToPage(
                    page = next,
                    animationSpec = tween(durationMillis = autoScrollAnimationMillis)
                )
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
            pageSpacing = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(carouselHeight)
        ) { page ->
            itemContent(page)
        }

        Surface(
            modifier = Modifier
                .padding(top = indicatorTopPadding),
            shape = CircleShape,
            color = indicatorContainerColor
        ) {
            DotsIndicator(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                totalDots = itemsCount,
                selectedIndex = pagerState.currentPage,
                selectedColor = selectedColor,
                unSelectedColor = unSelectedColor,
                dotSize = dotSize
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun AutoSlidingCarouselPreview() {
    PokedexAITheme {
        val items = listOf(
            Color.Red,
            Color.Green,
            Color.Blue
        )

        AutoSlidingCarousel(
            itemsCount = items.size,
            carouselHeight = 200.dp,
        ) { index ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(items[index]),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Page ${index + 1}",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}