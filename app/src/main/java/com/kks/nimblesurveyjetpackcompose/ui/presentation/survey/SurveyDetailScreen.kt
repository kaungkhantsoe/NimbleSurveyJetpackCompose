package com.kks.nimblesurveyjetpackcompose.ui.presentation.survey

import android.os.CountDownTimer
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieRetrySignal
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.model.Survey
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.ConfirmAlertDialog
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.ErrorAlertDialog
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.Loading
import com.kks.nimblesurveyjetpackcompose.ui.theme.BlackRussian
import com.kks.nimblesurveyjetpackcompose.ui.theme.NeuzeitFamily
import com.kks.nimblesurveyjetpackcompose.ui.theme.White70
import com.kks.nimblesurveyjetpackcompose.util.TWEEN_ANIM_TIME
import com.kks.nimblesurveyjetpackcompose.viewmodel.survey.SurveyDetailViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.launch

private const val SUBMIT_SUCCESS_LOTTIE_URL = "https://assets2.lottiefiles.com/packages/lf20_pmYw5P.json"
private const val LOTTIE_ENDS = 1.0f
private const val LOTTIE_FAIL_COUNT = 3

private const val ZOOM_DURATION_IN_MILLIS = 500L
private const val ZOOM_INTERVAL_IN_MILLIS = 25L
private const val SCALE_PER_COUNTDOWN = 0.025f
private const val ZOOMED_IN_SCALE = 1.5f
private const val ORIGINAL_SCALE = 1f

@OptIn(ExperimentalPagerApi::class)
@Destination
@Composable
fun SurveyDetailScreen(
    navigator: DestinationsNavigator,
    survey: Survey,
    viewModel: SurveyDetailViewModel = hiltViewModel()
) {
    val currentPage by viewModel.currentPage.collectAsState()
    val surveyQuestions by viewModel.surveyQuestions.collectAsState()
    val shouldShowLoading by viewModel.shouldShowLoading.collectAsState()
    val shouldShowThanks by viewModel.shouldShowThanks.collectAsState()
    val error by viewModel.error.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }
    val startSurveyDescription = stringResource(id = R.string.survey_detail_start_survey)
    val placeholderPainter = rememberAsyncImagePainter(model = survey.coverImagePlaceholderUrl)
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val isStartPage = currentPage == 0
    val isLastPage = currentPage == surveyQuestions.size

    var scale by remember { mutableStateOf(1f) }

    LaunchedEffect(key1 = Unit) {
        object : CountDownTimer(ZOOM_DURATION_IN_MILLIS, ZOOM_INTERVAL_IN_MILLIS) {
            override fun onTick(millisUntilFinished: Long) {
                scale += SCALE_PER_COUNTDOWN
            }

            override fun onFinish() {
                scale = ZOOMED_IN_SCALE
            }
        }.start()
    }

    val popBackAnimationTimer = object : CountDownTimer(ZOOM_DURATION_IN_MILLIS, ZOOM_INTERVAL_IN_MILLIS) {
        override fun onTick(millisUntilFinished: Long) {
            scale -= SCALE_PER_COUNTDOWN
        }

        override fun onFinish() {
            scale = ORIGINAL_SCALE
            navigator.popBackStack()
        }
    }

    BackHandler {
        if (currentPage > 0) showConfirmDialog = true
        else popBackAnimationTimer.start()
    }

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.setCurrentPage(page)
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getSurveyQuestions(surveyId = survey.id)
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AsyncImage(
            model = survey.coverImageFullUrl,
            contentDescription = stringResource(id = R.string.survey_detail_background_image),
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale
                ),
            placeholder = placeholderPainter,
            contentScale = ContentScale.Crop
        )
        Image(
            painter = painterResource(id = R.drawable.survey_overlay),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        HorizontalPager(
            count = surveyQuestions.size + 1,
            modifier = Modifier.padding(top = 108.dp),
            state = pagerState
        ) { page ->
            if (page == 0) {
                SurveyDetailStartScreen(survey, modifier = Modifier.fillMaxSize())
            } else {
                SurveyQuestionScreen(
                    surveyQuestion = surveyQuestions[page - 1],
                    pageNumber = page,
                    totalNumberOfPage = surveyQuestions.size
                ) { questionId, surveyAnswers ->
                    viewModel.setAnswers(questionId = questionId, answers = surveyAnswers)
                }
            }
        }
        SurveyToolbar(
            popBackAnimationTimer = popBackAnimationTimer,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(top = 57.dp, start = 4.dp, end = 15.dp),
            showBack = isStartPage,
            showClose = currentPage > 0,
            onClickClose = { showConfirmDialog = true }
        )
        StartOrSubmitButton(
            showButton = isStartPage || isLastPage,
            textRes = if (isStartPage) R.string.survey_detail_start_survey else R.string.survey_question_submit_survey,
            onClick = {
                if (isStartPage) {
                    scope.launch {
                        pagerState.animateScrollToPage(currentPage + 1)
                    }
                } else if (isLastPage) {
                    viewModel.submitSurvey(surveyId = survey.id)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 54.dp, end = 20.dp)
                .semantics { contentDescription = startSurveyDescription }
        )
        if (shouldShowThanks) LottieView(popBackAnimationTimer = popBackAnimationTimer)
        NextQuestionButton(
            showButton = !isStartPage && !isLastPage,
            onNextSlide = {
                scope.launch {
                    pagerState.animateScrollToPage(currentPage + 1)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 54.dp, end = 20.dp)
        )
        if (showConfirmDialog) {
            ConfirmAlertDialog(
                title = stringResource(id = R.string.survey_question_warning_dialog_title),
                message = stringResource(id = R.string.survey_question_warning_dialog_message),
                positiveButtonText = stringResource(id = R.string.survey_question_warning_dialog_yes),
                negativeButtonText = stringResource(id = R.string.survey_question_warning_dialog_cancel),
                onClickPositiveButton = {
                    showConfirmDialog = false
                    popBackAnimationTimer.start()
                },
                onClickNegativeButton = { showConfirmDialog = false }
            )
        }
        if (shouldShowLoading) Loading()
        error?.let { error ->
            ErrorAlertDialog(
                errorModel = error,
                title = stringResource(id = R.string.oops),
                buttonText = stringResource(id = android.R.string.ok),
                onClickButton = { viewModel.resetError() }
            )
        }
    }
}

@Composable
fun LottieView(popBackAnimationTimer: CountDownTimer) {
    val retrySignal = rememberLottieRetrySignal()
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Url(SUBMIT_SUCCESS_LOTTIE_URL),
        onRetry = { failCount, _ ->
            retrySignal.awaitRetry()
            failCount < LOTTIE_FAIL_COUNT
        }
    )
    val progress by animateLottieCompositionAsState(composition)

    if (progress == LOTTIE_ENDS) popBackAnimationTimer.start()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(composition)
        Text(
            text = stringResource(id = R.string.survey_question_thanks),
            fontFamily = NeuzeitFamily,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 28.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun StartOrSubmitButton(
    showButton: Boolean,
    textRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val startSurveyDescription = stringResource(id = R.string.survey_detail_start_survey)

    Crossfade(
        targetState = showButton,
        animationSpec = tween(TWEEN_ANIM_TIME),
        modifier = modifier.semantics { contentDescription = startSurveyDescription }
    ) {
        if (it) {
            Button(
                onClick = { onClick() },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.height(56.dp)
            ) {
                Text(
                    text = stringResource(id = textRes),
                    fontFamily = NeuzeitFamily,
                    fontWeight = FontWeight.Bold,
                    color = BlackRussian,
                    fontSize = 17.sp
                )
            }
        }
    }
}

@Composable
fun NextQuestionButton(
    showButton: Boolean,
    onNextSlide: () -> Unit,
    modifier: Modifier = Modifier
) {
    val nextQuestionDescription = stringResource(id = R.string.survey_question_next_question)
    Crossfade(
        targetState = showButton,
        animationSpec = tween(TWEEN_ANIM_TIME),
        modifier = modifier
    ) {
        if (it) {
            Button(
                onClick = { onNextSlide() },
                modifier = Modifier
                    .clip(CircleShape)
                    .size(56.dp)
                    .semantics { contentDescription = nextQuestionDescription },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_forward_ios_24),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun SurveyDetailStartScreen(survey: Survey, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SurveyBoldText(
            text = survey.title,
            fontSize = 28.sp,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        )
        Text(
            text = survey.description,
            fontFamily = NeuzeitFamily,
            fontWeight = FontWeight.Normal,
            color = White70,
            fontSize = 17.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
    }
}

@Composable
fun SurveyBoldText(
    text: String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        fontFamily = NeuzeitFamily,
        fontWeight = FontWeight.Bold,
        color = color,
        fontSize = fontSize,
        maxLines = maxLines,
        modifier = modifier,
        overflow = overflow,
        textAlign = textAlign
    )
}

@Composable
fun SurveyToolbar(
    popBackAnimationTimer: CountDownTimer,
    modifier: Modifier,
    showBack: Boolean,
    showClose: Boolean,
    onClickClose: () -> Unit
) {
    Box(modifier = modifier) {
        if (showBack) {
            IconButton(
                onClick = { popBackAnimationTimer.start() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back_accent),
                    contentDescription = stringResource(id = R.string.survey_detail_back_icon)
                )
            }
        }
        if (showClose) {
            IconButton(
                onClick = { onClickClose() },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_close_button_white),
                    contentDescription = stringResource(id = R.string.survey_detail_close_icon),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SurveyHomeDetailScreenPreview() {
    SurveyDetailScreen(
        navigator = EmptyDestinationsNavigator,
        survey = Survey(
            id = "",
            coverImagePlaceholderUrl = "https://dhdbhh0jsld0o.cloudfront.net/m/c96c480fc8b69d50e75a_",
            title = "Tree Tops Australia",
            description = "We'd love to hear from you!"
        )
    )
}
