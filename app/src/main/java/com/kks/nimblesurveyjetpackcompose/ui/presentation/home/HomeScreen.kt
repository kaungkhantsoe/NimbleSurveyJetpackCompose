package com.kks.nimblesurveyjetpackcompose.ui.presentation.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.model.entities.Survey
import com.kks.nimblesurveyjetpackcompose.model.entities.Survey
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.DotsIndicator
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.ErrorAlertDialog
import com.kks.nimblesurveyjetpackcompose.ui.presentation.splash.TWEEN_ANIM_TIME
import com.kks.nimblesurveyjetpackcompose.ui.theme.NeuzeitFamily
import com.kks.nimblesurveyjetpackcompose.ui.theme.White20
import com.kks.nimblesurveyjetpackcompose.ui.theme.White70
import com.kks.nimblesurveyjetpackcompose.util.DateUtil
import com.kks.nimblesurveyjetpackcompose.viewmodel.home.HomeViewModel
import com.ramcosta.composedestinations.annotation.Destination

private const val FRACTION = 0.3f
private const val IDLE = 0
private const val LEFT_SWIPE = 1
private const val RIGHT_SWIPE = -1
private const val START_SURVEY_NUMBER = 0
private const val START_ANCHOR = 0f
private const val LEFT_STATE = "L"
private const val RIGHT_STATE = "R"
private const val MID_STATE = "M"

@Suppress("ComplexCondition", "LongMethod", "ComplexMethod")
@OptIn(ExperimentalMaterialApi::class)
@Destination
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val activity = LocalContext.current as? Activity

    val surveyList by viewModel.surveyList.collectAsState()
    val error by viewModel.error.collectAsState()
    var selectedSurveyNumber by remember { mutableStateOf(START_SURVEY_NUMBER) }
    val swipeableState = rememberSwipeableState(initialValue = MID_STATE)
    val endAnchor = LocalDensity.current.run { LocalConfiguration.current.screenWidthDp.toDp().toPx() }
    // Maps anchor points (in px) to states
    val anchors = mapOf(START_ANCHOR to LEFT_STATE, endAnchor / 2 to MID_STATE, endAnchor to RIGHT_STATE)
    var threshold by remember { mutableStateOf(IDLE) }
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val userAvatar by viewModel.userAvatar.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {
            // TODO: Implement swipe refresh here
        },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            if (isRefreshing && surveyList.isEmpty()) {
                item { HomeScreenShimmerLoading(modifier = Modifier.fillParentMaxSize()) }
            }
            if (surveyList.isNotEmpty() && error == null) {
                item {
                    LaunchedEffect(keys = arrayOf(swipeableState.offset.value), block = {
                        val currentSwipeState = swipeableState.offset.value
                        if (currentSwipeState < endAnchor / 2 && threshold == IDLE) {
                            // Swipe to left
                            threshold = LEFT_SWIPE
                        } else if (currentSwipeState > endAnchor / 2 && threshold == IDLE) {
                            // Swipe to right
                            threshold = RIGHT_SWIPE
                        } else if ((currentSwipeState == endAnchor / 2)) {
                            if (
                                (selectedSurveyNumber < surveyList.size - 1 && threshold == LEFT_SWIPE) ||
                                (selectedSurveyNumber != 0 && threshold == RIGHT_SWIPE)
                            ) {
                                selectedSurveyNumber += threshold
                                if (selectedSurveyNumber == (surveyList.size) - 2) viewModel.getNextPage()
                            }
                            threshold = IDLE
                        }
                        swipeableState.snapTo(targetValue = MID_STATE)
                    })
                    SurveyContent(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .swipeable(
                                state = swipeableState,
                                anchors = anchors,
                                thresholds = { _, _ -> FractionalThreshold(fraction = FRACTION) },
                                orientation = Orientation.Horizontal
                            ),
                        numberOfPage = surveyList.size,
                        selectedSurveyNumber = selectedSurveyNumber,
                        survey = surveyList[selectedSurveyNumber],
                        userAvatar = userAvatar
                    )
                }
            }
            error?.let { error ->
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        ErrorAlertDialog(
                            errorModel = error,
                            title = stringResource(id = R.string.oops),
                            buttonText = stringResource(id = android.R.string.ok),
                            onClickButton = { viewModel.resetError() }
                        )
                    }
                }
            }
        }
    }
    BackHandler {
        activity?.finish()
    }
}

@Composable
fun SurveyContent(
    modifier: Modifier,
    survey: Survey,
    numberOfPage: Int,
    selectedSurveyNumber: Int,
    userAvatar: String?
) {
    ConstraintLayout(modifier = modifier) {
        val (date, userImage, bottomView) = createRefs()
        SurveyImage(imageUrl = survey.coverImageUrl)
        Image(
            painter = painterResource(id = R.drawable.survey_overlay),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.constrainAs(date) {
                top.linkTo(parent.top, 60.dp)
                start.linkTo(parent.start, 20.dp)
            },
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            SurveyText(
                text = DateUtil.getBeautifiedCurrentDate(),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            SurveyText(
                text = stringResource(id = R.string.home_today),
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )
        }
        UserIcon(
            modifier = Modifier
                .clip(CircleShape)
                .size(36.dp)
                .constrainAs(userImage) {
                    end.linkTo(parent.end, 20.dp)
                    top.linkTo(parent.top, 79.dp)
                },
            userAvatar = userAvatar
        )
        BottomView(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(bottomView) { bottom.linkTo(parent.bottom, 54.dp) },
            numberOfPage = numberOfPage,
            currentPage = selectedSurveyNumber,
            survey = survey
        )
    }
}

@Composable
fun SurveyImage(imageUrl: String) {
    Crossfade(
        targetState = imageUrl,
        animationSpec = tween(TWEEN_ANIM_TIME)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(it)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(id = R.string.home_survey_background_image),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun UserIcon(userAvatar: String?, modifier: Modifier) {
    AsyncImage(
        model = userAvatar,
        placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = stringResource(
            id = R.string.home_user_image
        ),
        modifier = modifier
    )
}

@Composable
fun BottomView(
    survey: Survey,
    numberOfPage: Int,
    currentPage: Int,
    modifier: Modifier
) {
    Column(modifier = modifier) {
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
            text = survey.title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SurveyText(
                text = survey.description,
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

@Composable
fun SurveyText(
    text: String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = Color.White
) {
    Crossfade(
        targetState = text,
        animationSpec = tween(TWEEN_ANIM_TIME)
    ) {
        Text(
            text = it,
            fontFamily = NeuzeitFamily,
            fontWeight = fontWeight,
            color = color,
            fontSize = fontSize,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeContentPreview() {
    HomeScreen()
}
