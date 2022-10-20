package com.kks.nimblesurveyjetpackcompose.ui.presentation.survey

import android.os.CountDownTimer
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.model.Survey
import com.kks.nimblesurveyjetpackcompose.model.SurveyAnswer
import com.kks.nimblesurveyjetpackcompose.model.SurveyQuestion
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.ConfirmAlertDialog
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.ErrorAlertDialog
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.Loading
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.LottieView
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.SurveyToolbar
import com.kks.nimblesurveyjetpackcompose.ui.theme.BlackRussian
import com.kks.nimblesurveyjetpackcompose.ui.theme.NeuzeitFamily
import com.kks.nimblesurveyjetpackcompose.util.TWEEN_ANIM_TIME
import com.kks.nimblesurveyjetpackcompose.viewmodel.survey.SurveyDetailViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val ZOOM_DURATION_IN_MILLIS = 500L
private const val ZOOM_INTERVAL_IN_MILLIS = 25L
private const val SCALE_PER_COUNTDOWN = 0.025f
private const val ZOOMED_IN_SCALE = 1.5f
private const val ORIGINAL_SCALE = 1f
private const val DELAY_TO_CLEAR_FOCUS = 500L

@OptIn(ExperimentalComposeUiApi::class)
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
    var scale by remember { mutableStateOf(1f) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        object : CountDownTimer(ZOOM_DURATION_IN_MILLIS, ZOOM_INTERVAL_IN_MILLIS) {
            override fun onTick(millisUntilFinished: Long) {
                scale += SCALE_PER_COUNTDOWN
            }

            override fun onFinish() {
                scale = ZOOMED_IN_SCALE
            }
        }.start()
        viewModel.getSurveyQuestions(surveyId = survey.id)
    }

    val zoomOut = {
        object : CountDownTimer(ZOOM_DURATION_IN_MILLIS, ZOOM_INTERVAL_IN_MILLIS) {
            override fun onTick(millisUntilFinished: Long) {
                scale -= SCALE_PER_COUNTDOWN
            }

            override fun onFinish() {
                scale = ORIGINAL_SCALE
                navigator.popBackStack()
            }
        }.start()
    }

    BackHandler {
        if (currentPage > 0) showConfirmDialog = true
        else zoomOut()
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                    // Need to wait for keyboard hiding before clearFocus. If not the screen doesn't resize.
                    coroutineScope.launch {
                        delay(DELAY_TO_CLEAR_FOCUS)
                        focusManager.clearFocus()
                    }
                })
            },
        contentAlignment = Alignment.Center
    ) {
        SurveyQuestionDetailContent(
            survey = survey,
            surveyQuestions = surveyQuestions,
            currentPage = currentPage,
            scale = scale,
            onSetCurrentPage = { viewModel.setCurrentPage(it) },
            onSubmitSurvey = { viewModel.submitSurvey(surveyId = survey.id) },
            onSetAnswers = { questionId, surveyAnswers ->
                viewModel.setAnswers(questionId = questionId, answers = surveyAnswers)
            },
            onClickClose = { showConfirmDialog = true },
            onPopBack = { zoomOut() }
        )
        if (shouldShowThanks) LottieView(onLottieEnds = { navigator.popBackStack() })
        if (showConfirmDialog) {
            ConfirmAlertDialog(
                title = stringResource(id = R.string.survey_question_warning_dialog_title),
                message = stringResource(id = R.string.survey_question_warning_dialog_message),
                positiveButtonText = stringResource(id = R.string.survey_question_warning_dialog_yes),
                negativeButtonText = stringResource(id = R.string.survey_question_warning_dialog_cancel),
                onClickPositiveButton = {
                    showConfirmDialog = false
                    zoomOut()
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

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SurveyQuestionDetailContent(
    survey: Survey,
    surveyQuestions: List<SurveyQuestion>,
    scale: Float,
    currentPage: Int,
    onSetCurrentPage: (page: Int) -> Unit,
    onSubmitSurvey: () -> Unit,
    onSetAnswers: (questionId: String, answers: List<SurveyAnswer>) -> Unit,
    onClickClose: () -> Unit,
    onPopBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val isStartPage = currentPage == 0
    val isLastPage = currentPage == surveyQuestions.size
    val startSurveyDescription = stringResource(id = R.string.survey_detail_start_survey)
    val placeholderPainter = rememberAsyncImagePainter(model = survey.coverImagePlaceholderUrl)
    val pagerState = rememberPagerState()

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onSetCurrentPage(page)
        }
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
                    onSetAnswers(questionId, surveyAnswers)
                }
            }
        }
        SurveyToolbar(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(top = 57.dp, start = 4.dp, end = 15.dp),
            showBack = isStartPage,
            showClose = currentPage > 0,
            onClickClose = { onClickClose() },
            onPopBack = { onPopBack() }
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
                    onSubmitSurvey()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 54.dp, end = 20.dp)
                .semantics { contentDescription = startSurveyDescription }
        )
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

@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun SurveyHomeDetailScreenPreview() {
    SurveyQuestionDetailContent(
        survey = Survey(
            id = "",
            coverImagePlaceholderUrl = "https://dhdbhh0jsld0o.cloudfront.net/m/c96c480fc8b69d50e75a_",
            title = "Tree Tops Australia",
            description = "We'd love to hear from you!"
        ),
        surveyQuestions = emptyList(),
        scale = 0f,
        currentPage = 1,
        onSetCurrentPage = {},
        onSubmitSurvey = {},
        onSetAnswers = { _, _ ->

        },
        onClickClose = {},
        onPopBack = {}
    )
}
