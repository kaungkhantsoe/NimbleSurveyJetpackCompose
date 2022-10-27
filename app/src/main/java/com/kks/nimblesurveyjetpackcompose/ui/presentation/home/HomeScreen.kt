package com.kks.nimblesurveyjetpackcompose.ui.presentation.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.model.Survey
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.DayNightPreviews
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.DotsIndicator
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.ErrorAlertDialog
import com.kks.nimblesurveyjetpackcompose.ui.presentation.destinations.SurveyDetailScreenDestination
import com.kks.nimblesurveyjetpackcompose.ui.theme.NeuzeitFamily
import com.kks.nimblesurveyjetpackcompose.ui.theme.White20
import com.kks.nimblesurveyjetpackcompose.ui.theme.White70
import com.kks.nimblesurveyjetpackcompose.util.DateUtil
import com.kks.nimblesurveyjetpackcompose.util.TWEEN_ANIM_TIME
import com.kks.nimblesurveyjetpackcompose.viewmodel.home.HomeUiState
import com.kks.nimblesurveyjetpackcompose.viewmodel.home.HomeUiStatePreviewParameterProvider
import com.kks.nimblesurveyjetpackcompose.viewmodel.home.HomeViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

private const val FRACTION = 0.3f
private const val IDLE = 0
private const val LEFT_SWIPE = 1
private const val RIGHT_SWIPE = -1
private const val START_ANCHOR = 0f
private const val LEFT_STATE = "L"
private const val RIGHT_STATE = "R"
private const val MID_STATE = "M"

@Destination
@Composable
fun HomeScreen(navigator: DestinationsNavigator, viewModel: HomeViewModel = hiltViewModel()) {

    val homeUiState by viewModel.homeUiState.collectAsState()
    val activity = LocalContext.current as? Activity

    HomeScreenContent(
        homeUiState = homeUiState,
        onRefresh = { viewModel.clearCacheAndFetch() },
        onSwipe = {
            viewModel.setSelectedSurveyNumber(it)
            if (homeUiState.selectedSurveyNumber == (homeUiState.surveyList.size) - 2) viewModel.getNextPage()
        },
        onResetError = { viewModel.resetError() },
        onClickSurveyDetail = {
            navigator.navigate(
                SurveyDetailScreenDestination(survey = homeUiState.surveyList[homeUiState.selectedSurveyNumber])
            )
        }
    )
    BackHandler {
        activity?.finish()
    }
}

@Suppress("ComplexCondition", "ComplexMethod")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreenContent(
    homeUiState: HomeUiState,
    onClickSurveyDetail: () -> Unit,
    onRefresh: () -> Unit,
    onSwipe: (page: Int) -> Unit,
    onResetError: () -> Unit,
) {
    val swipeableState = rememberSwipeableState(initialValue = MID_STATE)
    val endAnchor = LocalDensity.current.run { LocalConfiguration.current.screenWidthDp.toDp().toPx() }
    // Maps anchor points (in px) to states
    val anchors = mapOf(START_ANCHOR to LEFT_STATE, endAnchor / 2 to MID_STATE, endAnchor to RIGHT_STATE)
    var threshold by remember { mutableStateOf(IDLE) }

    homeUiState.apply {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                if (isRefreshing && surveyList.isEmpty()) {
                    item { HomeScreenShimmerLoading(modifier = Modifier.fillParentMaxSize()) }
                }
                if (surveyList.isNotEmpty()) {
                    item {
                        LaunchedEffect(keys = arrayOf(swipeableState.offset.value), block = {
                            val currentSwipeState = swipeableState.offset.value
                            when {
                                currentSwipeState < endAnchor / 2 && threshold == IDLE -> {
                                    // Swipe to left
                                    threshold = LEFT_SWIPE
                                }
                                currentSwipeState > endAnchor / 2 && threshold == IDLE -> {
                                    // Swipe to right
                                    threshold = RIGHT_SWIPE
                                }
                                currentSwipeState == endAnchor / 2 -> {
                                    if ((selectedSurveyNumber < surveyList.size - 1 && threshold == LEFT_SWIPE) ||
                                        (selectedSurveyNumber != 0 && threshold == RIGHT_SWIPE)
                                    ) {
                                        onSwipe(selectedSurveyNumber + threshold)
                                    }
                                    threshold = IDLE
                                }
                            }
                            swipeableState.snapTo(targetValue = MID_STATE)
                        })
                        SurveyContent(
                            survey = homeUiState.surveyList[selectedSurveyNumber],
                            userAvatar = homeUiState.userAvatar.orEmpty(),
                            numberOfPage = homeUiState.surveyList.size,
                            selectedSurveyNumber = selectedSurveyNumber,
                            modifier = Modifier
                                .fillParentMaxSize()
                                .swipeable(
                                    state = swipeableState,
                                    anchors = anchors,
                                    thresholds = { _, _ -> FractionalThreshold(fraction = FRACTION) },
                                    orientation = Orientation.Horizontal
                                ),
                            onClickSurveyDetail = onClickSurveyDetail
                        )
                    }
                }
                error?.let { error ->
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            ErrorAlertDialog(
                                errorModel = error,
                                title = stringResource(id = R.string.oops),
                                buttonText = stringResource(id = android.R.string.ok),
                                onClickButton = onResetError
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SurveyContent(
    survey: Survey,
    userAvatar: String,
    numberOfPage: Int,
    selectedSurveyNumber: Int,
    onClickSurveyDetail: () -> Unit,
    modifier: Modifier = Modifier
) {
    val surveyContentDescription = stringResource(id = R.string.home_survey_content)

    ConstraintLayout(modifier = modifier.semantics { contentDescription = surveyContentDescription }) {
        val (date, userImage, bottomView) = createRefs()
        SurveyImage(imageUrl = survey.coverImageFullUrl, placeholderUrl = survey.coverImagePlaceholderUrl)
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
            SurveyCrossfadeText(
                text = DateUtil.getBeautifiedDate(),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            SurveyCrossfadeText(
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
            survey = survey,
            onClickSurveyDetail = onClickSurveyDetail
        )
    }
}

@Composable
fun SurveyImage(imageUrl: String, placeholderUrl: String) {
    val placeholderPainter = rememberAsyncImagePainter(model = placeholderUrl)

    Crossfade(
        targetState = imageUrl,
        animationSpec = tween(TWEEN_ANIM_TIME)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(it)
                .crossfade(true)
                .build(),
            placeholder = placeholderPainter,
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
        placeholder = painterResource(id = R.drawable.ic_baseline_android_24),
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
    onClickSurveyDetail: () -> Unit,
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
        SurveyCrossfadeText(
            text = survey.title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp),
        )
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (description, detailBtn) = createRefs()
            SurveyCrossfadeText(
                text = survey.description,
                fontSize = 17.sp,
                color = White70,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .constrainAs(description) {
                        start.linkTo(parent.start)
                        end.linkTo(detailBtn.start, 20.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }
            )
            SurveyRoundedButton(
                modifier = Modifier.constrainAs(detailBtn) {
                    end.linkTo(parent.end, 20.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                onClick = onClickSurveyDetail
            )
            Spacer(modifier = Modifier.width(20.dp))
        }
    }
}

@Composable
fun SurveyRoundedButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier
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

@Composable
fun SurveyCrossfadeText(
    text: String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = Color.White
) {
    Crossfade(
        targetState = text,
        animationSpec = tween(TWEEN_ANIM_TIME),
        modifier = modifier
    ) {
        Text(
            text = it,
            fontFamily = NeuzeitFamily,
            fontWeight = fontWeight,
            color = color,
            fontSize = fontSize,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@DayNightPreviews
@Composable
fun HomeContentPreview(
    @PreviewParameter(HomeUiStatePreviewParameterProvider::class) homeUiState: HomeUiState
) {
    HomeScreenContent(homeUiState = homeUiState, {}, {}, {}, {})
}
