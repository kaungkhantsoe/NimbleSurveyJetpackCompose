package com.kks.nimblesurveyjetpackcompose.ui.presentation.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun HomeScreenShimmerLoading() {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.4f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.4f),
    )

    val transition = rememberInfiniteTransition()
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )

    ShimmerHomeScreen(brush = brush)
}

@Composable
fun ShimmerHomeScreen(brush: Brush) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        val (topTexts, bottomTexts, circle) = createRefs()

        Spacer(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(brush)
                .constrainAs(circle) {
                    top.linkTo(parent.top, 79.dp)
                    end.linkTo(parent.end, 20.dp)
                }
        )
        Column(
            Modifier.constrainAs(topTexts) {
                top.linkTo(parent.top, 61.dp)
                start.linkTo(parent.start, 20.dp)
            }
        ) {
            CustomSpacerRectangle(brush = brush, width = 117.dp)
            CustomSpacerRectangle(brush = brush, width = 80.dp, topPadding = 15.dp)
        }

        Column(
            Modifier.constrainAs(bottomTexts) {
                bottom.linkTo(parent.bottom, 62.dp)
                start.linkTo(parent.start, 20.dp)
            }
        ) {
            CustomSpacerRectangle(brush = brush, width = 42.dp)
            CustomSpacerRectangle(brush = brush, width = 253.dp, topPadding = 15.dp)
            CustomSpacerRectangle(brush = brush, width = 126.dp, topPadding = 10.dp)
            CustomSpacerRectangle(brush = brush, width = 318.dp, topPadding = 15.dp)
            CustomSpacerRectangle(brush = brush, width = 170.dp, topPadding = 10.dp)
        }
    }
}

@Composable
fun CustomSpacerRectangle(brush: Brush, width: Dp, topPadding: Dp = 0.dp) {
    Column {
        Spacer(modifier = Modifier.height(topPadding))
        Spacer(
            modifier = Modifier
                .size(width = width, height = 20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(brush)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ShimmerHomeScreenPreview() {
    ShimmerHomeScreen(
        brush = Brush.linearGradient(
            listOf(
                Color.LightGray.copy(alpha = 0.4f),
                Color.LightGray.copy(alpha = 0.2f),
                Color.LightGray.copy(alpha = 0.4f),
            )
        )
    )
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun ShimmerHomeScreenPreviewNight() {
    ShimmerHomeScreen(
        brush = Brush.linearGradient(
            listOf(
                Color.LightGray.copy(alpha = 0.4f),
                Color.LightGray.copy(alpha = 0.2f),
                Color.LightGray.copy(alpha = 0.4f),
            )
        )
    )
}
