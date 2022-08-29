package com.kks.nimblesurveyjetpackcompose.ui.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kks.nimblesurveyjetpackcompose.ui.theme.White20

// Reference https://medium.com/@prashantappdeveloper/viewpager-in-jetpack-compose-with-dot-indicators-within-minutes-a2779970534e
@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color,
    unSelectedColor: Color,
    indicatorSize: Dp,
    space: Dp,
) {
    val listState = rememberLazyListState()
    LazyRow(
        state = listState,
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(horizontal = 20.dp)
    ) {
        items(totalDots) { index ->
            if (index == selectedIndex) {
                Box(
                    modifier = Modifier
                        .size(indicatorSize)
                        .clip(CircleShape)
                        .background(selectedColor)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(indicatorSize)
                        .clip(CircleShape)
                        .background(unSelectedColor)
                )
            }
            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = space))
            }
        }
    }
    LaunchedEffect(key1 = selectedIndex) {
        listState.scrollToItem(selectedIndex)
    }
}

@Preview(showBackground = true)
@Composable
fun DotsIndicatorPreview() {
    DotsIndicator(
        totalDots = 3,
        selectedIndex = 0,
        selectedColor = Color.White,
        unSelectedColor = White20,
        indicatorSize = 8.dp,
        space = 5.dp
    )
}
