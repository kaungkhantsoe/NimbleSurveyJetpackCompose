package com.kks.nimblesurveyjetpackcompose.ui.presentation.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.HomeScreenShimmerLoading
import com.kks.nimblesurveyjetpackcompose.ui.theme.White20
import com.kks.nimblesurveyjetpackcompose.ui.theme.White70
import com.kks.nimblesurveyjetpackcompose.util.DotsIndicator
import com.kks.nimblesurveyjetpackcompose.util.neuzeitFamily
import com.ramcosta.composedestinations.annotation.Destination

private const val FRACTION = 0.3f
private const val IDLE = 0
private const val LEFT_SWIPE = 1
private const val RIGHT_SWIPE = -1

// TODO: Replace texts and image placeholder when integrate
@Destination
@Composable
fun HomeScreen() {
    val activity = LocalContext.current as? Activity

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        HomeScreenShimmerLoading()
        HomeContent()
    }
    BackHandler {
        activity?.finish()
    }
}

@Suppress(
    "LongMethod",
    "DestructuringDeclarationWithTooManyEntries",
    "ComplexCondition",
    "MagicNumber"
)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeContent() {
    val numberOfPage by remember { mutableStateOf(3) }
    var currentPage by remember { mutableStateOf(0) }
    val swipeableState = rememberSwipeableState(initialValue = "M")
    val sizePx = LocalDensity.current.run { LocalConfiguration.current.screenWidthDp.toDp().toPx() }
    // Maps anchor points (in px) to states
    val anchors = mapOf(0f to "L", sizePx / 2 to "M", sizePx to "R")
    var threshold by remember { mutableStateOf(IDLE) }

    LaunchedEffect(keys = arrayOf(swipeableState.offset.value), block = {
        val currentSwipeState = swipeableState.offset.value
        if (currentSwipeState < sizePx / 2 && threshold == IDLE) {
            // Swipe to left
            threshold = LEFT_SWIPE
        } else if (currentSwipeState > sizePx / 2 && threshold == IDLE) {
            // Swipe to right
            threshold = RIGHT_SWIPE
        } else if ((currentSwipeState == sizePx / 2)) {
            if (
                (currentPage < numberOfPage - 1 && threshold == LEFT_SWIPE) ||
                (currentPage != 0 && threshold == RIGHT_SWIPE)
            ) {
                currentPage += threshold
            }
            threshold = IDLE
        }
        swipeableState.snapTo(targetValue = "M")
    })
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(fraction = FRACTION) },
                orientation = Orientation.Horizontal
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { /* Called when the gesture starts */ }
                )
            }
    ) {
        val (date, today, userImage, bottomView, backgroundImage) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = stringResource(id = R.string.home_survey_background_image),
            modifier = Modifier
                .constrainAs(backgroundImage) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        SurveyText(
            text = "MONDAY,JUNE 15",
            modifier = Modifier.constrainAs(date) {
                top.linkTo(parent.top, 60.dp)
                start.linkTo(parent.start, 20.dp)
            },
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        SurveyText(
            text = "Today",
            modifier = Modifier.constrainAs(today) {
                top.linkTo(date.bottom, 4.dp)
                start.linkTo(parent.start, 20.dp)
            },
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(
                id = R.string.home_user_image
            ),
            modifier = Modifier
                .clip(CircleShape)
                .size(36.dp)
                .constrainAs(userImage) {
                    end.linkTo(parent.end, 20.dp)
                    top.linkTo(parent.top, 79.dp)
                }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(bottomView) {
                    bottom.linkTo(parent.bottom, 54.dp)
                }
        ) {
            DotsIndicator(
                totalDots = numberOfPage,
                selectedIndex = currentPage,
                selectedColor = Color.White,
                unSelectedColor = White20,
                indicatorSize = 8.dp,
                space = 5.dp
            )
            Spacer(modifier = Modifier.height(26.dp))
            SurveyText(
                text = "Working from home Check-In",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SurveyText(
                    text = "We would like to know how you feel about our work from home...",
                    fontSize = 17.sp,
                    color = White70,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 20.dp, end = 20.dp)
                )
                Button(
                    onClick = { },
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(56.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_forward_ios_24),
                        contentDescription = stringResource(id = R.string.home_survey_detail_button)
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
            }
        }
    }
}

@Composable
fun SurveyText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = Color.White
) {
    Text(
        text = text,
        fontFamily = neuzeitFamily,
        fontWeight = fontWeight,
        color = color,
        fontSize = fontSize,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun HomeContentPreview() {
    HomeContent()
}
