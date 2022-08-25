package com.kks.nimblesurveyjetpackcompose.ui.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Reference https://medium.com/@prashantappdeveloper/viewpager-in-jetpack-compose-with-dot-indicators-within-minutes-a2779970534e
@Suppress("LongParameterList")
@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color,
    unSelectedColor: Color,
    indicatorSize: Dp,
    space: Dp
) {
    LazyRow(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(start = 20.dp, end = 20.dp)
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
}
