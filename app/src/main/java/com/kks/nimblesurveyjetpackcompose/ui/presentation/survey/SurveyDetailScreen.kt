package com.kks.nimblesurveyjetpackcompose.ui.presentation.survey

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
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
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.ConfirmAlertDialog
import com.kks.nimblesurveyjetpackcompose.ui.theme.BlackRussian
import com.kks.nimblesurveyjetpackcompose.ui.theme.NeuzeitFamily
import com.kks.nimblesurveyjetpackcompose.ui.theme.White70
import com.kks.nimblesurveyjetpackcompose.util.TWEEN_ANIM_TIME
import com.kks.nimblesurveyjetpackcompose.viewmodel.survey.SurveyDetailViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Destination
@Composable
fun SurveyDetailScreen(
    navigator: DestinationsNavigator,
    survey: Survey,
    viewModel: SurveyDetailViewModel = hiltViewModel()
) {
    val currentPage by viewModel.currentPage.collectAsState()
    val surveyQuestionList by viewModel.surveyQuestionList.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }
    val startSurveyDescription = stringResource(id = R.string.survey_detail_start_survey)
    val placeholderPainter = rememberAsyncImagePainter(model = survey.coverImagePlaceholderUrl)
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.setCurrentPage(page)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = survey.coverImageFullUrl,
            contentDescription = stringResource(id = R.string.survey_detail_background_image),
            modifier = Modifier.fillMaxSize(),
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
            count = surveyQuestionList.size + 1,
            modifier = Modifier.padding(top = 108.dp),
            state = pagerState
        ) { page ->
            if (page == 0) {
                SurveyDetailStartScreen(survey, modifier = Modifier.fillMaxSize())
            } else {
                SurveyQuestionScreen(surveyQuestion = surveyQuestionList[page - 1])
            }
        }
        SurveyToolbar(
            navigator = navigator,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(top = 57.dp, start = 4.dp, end = 15.dp),
            showBack = currentPage == 0,
            showClose = currentPage > 0,
            onClickClose = { showConfirmDialog = true }
        )
        StartSurveyBtn(
            showButton = currentPage == 0,
            onNextSlide = {
                scope.launch {
                    pagerState.animateScrollToPage(currentPage + 1)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 54.dp, end = 20.dp)
                .semantics { contentDescription = startSurveyDescription }
        )
        NextQuestionBtn(
            showButton = currentPage > 0,
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
                title = "Warning!",
                message = "Are you sure you want to quit the survey?",
                positiveBtnText = "Yes",
                negativeBtnText = "Cancel",
                onClickPositiveBtn = {
                    showConfirmDialog = false
                    navigator.popBackStack()
                },
                onClickNegativeBtn = { showConfirmDialog = false }
            )
        }
    }
}

@Composable
fun StartSurveyBtn(
    showButton: Boolean,
    onNextSlide: () -> Unit,
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
                onClick = { onNextSlide() },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.height(56.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.survey_detail_start_survey),
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
fun NextQuestionBtn(
    showButton: Boolean,
    onNextSlide: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    .size(56.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_forward_ios_24),
                    contentDescription = stringResource(id = R.string.home_survey_detail_button)
                )
            }
        }
    }
}

@Composable
fun SurveyDetailStartScreen(survey: Survey, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SurveyTitleText(text = survey.title, fontSize = 28.sp, modifier = Modifier.padding(horizontal = 20.dp))
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
fun SurveyTitleText(text: String, fontSize: TextUnit, modifier: Modifier = Modifier, color: Color = Color.White) {
    Text(
        text = text,
        fontFamily = NeuzeitFamily,
        fontWeight = FontWeight.Bold,
        color = color,
        fontSize = fontSize,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun SurveyToolbar(
    navigator: DestinationsNavigator,
    modifier: Modifier,
    showBack: Boolean,
    showClose: Boolean,
    onClickClose: () -> Unit
) {
    Box(modifier = modifier) {
        if (showBack) {
            IconButton(
                onClick = { navigator.popBackStack() },
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
