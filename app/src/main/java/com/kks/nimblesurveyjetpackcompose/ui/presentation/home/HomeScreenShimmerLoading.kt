package com.kks.nimblesurveyjetpackcompose.ui.presentation.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

private const val INITIAL_VALUE = 0f
private const val TARGET_VALUE = 1000f
private const val ANIMATE_DURATION = 1000

@Composable
fun HomeScreenShimmerLoading(modifier: Modifier) {
    val transition = rememberInfiniteTransition()
    val translateAnimation = transition.animateFloat(
        initialValue = INITIAL_VALUE,
        targetValue = TARGET_VALUE,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = ANIMATE_DURATION,
                easing = FastOutSlowInEasing
            )
        )
    )

    ShimmerHomeScreen(brush = shimmerBrush(translateAnimation), modifier = modifier)
}

@Suppress("DestructuringDeclarationWithTooManyEntries")
@Composable
fun ShimmerHomeScreen(brush: Brush, modifier: Modifier) {
    ConstraintLayout(
        modifier = modifier.background(Color.Black)
    ) {
        val (text1, text2, text3, text4, text5, text6, text7, circle) = createRefs()

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
        CustomSpacerRectangle(
            brush = brush,
            modifier = Modifier
                .size(width = 117.dp, height = 20.dp)
                .constrainAs(text1) {
                    top.linkTo(parent.top, 61.dp)
                    start.linkTo(parent.start, 20.dp)
                }
        )
        CustomSpacerRectangle(
            brush = brush,
            modifier = Modifier
                .size(width = 80.dp, height = 20.dp)
                .constrainAs(text2) {
                    top.linkTo(text1.bottom, 15.dp)
                    start.linkTo(text1.start)
                }
        )

        CustomSpacerRectangle(
            brush = brush,
            modifier = Modifier
                .size(width = 42.dp, height = 20.dp)
                .constrainAs(text3) {
                    start.linkTo(text4.start)
                    bottom.linkTo(text4.top, 15.dp)
                }
        )
        CustomSpacerRectangle(
            brush = brush,
            modifier = Modifier
                .size(width = 253.dp, height = 20.dp)
                .constrainAs(text4) {
                    start.linkTo(text5.start)
                    bottom.linkTo(text5.top, 10.dp)
                }
        )
        CustomSpacerRectangle(
            brush = brush,
            modifier = Modifier
                .size(width = 126.dp, height = 20.dp)
                .constrainAs(text5) {
                    start.linkTo(text6.start)
                    bottom.linkTo(text6.top, 15.dp)
                }
        )
        CustomSpacerRectangle(
            brush = brush,
            modifier = Modifier
                .size(width = 318.dp, height = 20.dp)
                .constrainAs(text6) {
                    start.linkTo(text7.start)
                    bottom.linkTo(text7.top, 10.dp)
                }
        )
        CustomSpacerRectangle(
            brush = brush,
            modifier = Modifier
                .size(width = 170.dp, height = 20.dp)
                .constrainAs(text7) {
                    bottom.linkTo(parent.bottom, 62.dp)
                    start.linkTo(parent.start, 20.dp)
                }
        )
    }
}

@Composable
fun shimmerBrush(translateAnimation: State<Float> = mutableStateOf(0f)) =
    Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.4f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.4f),
        ),
        start = Offset.Zero,
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )

@Composable
fun CustomSpacerRectangle(brush: Brush, modifier: Modifier) =
    Spacer(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(brush)
    )

@Composable
@Preview(showBackground = true)
fun ShimmerHomeScreenPreview() {
    ShimmerHomeScreen(brush = shimmerBrush(), modifier = Modifier.fillMaxSize())
}
