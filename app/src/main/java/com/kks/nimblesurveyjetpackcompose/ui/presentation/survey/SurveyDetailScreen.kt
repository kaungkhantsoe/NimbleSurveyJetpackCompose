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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.model.Survey
import com.kks.nimblesurveyjetpackcompose.ui.theme.BlackRussian
import com.kks.nimblesurveyjetpackcompose.ui.theme.NeuzeitFamily
import com.kks.nimblesurveyjetpackcompose.ui.theme.White70
import com.kks.nimblesurveyjetpackcompose.util.TWEEN_ANIM_TIME
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@OptIn(ExperimentalPagerApi::class)
@Destination
@Composable
fun SurveyDetailScreen(navigator: DestinationsNavigator, survey: Survey) {
    var currentPage by remember { mutableStateOf(0) }
    val startSurveyDescription = stringResource(id = R.string.survey_detail_start_survey)
    val placeholderPainter = rememberAsyncImagePainter(model = survey.coverImagePlaceholderUrl)

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
            count = 1,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 108.dp),
            userScrollEnabled = false
        ) { page ->
            currentPage = page
            if (page == 0) {
                SurveyDetailStartScreen(survey, modifier = Modifier.fillMaxWidth())
            }
        }
        SurveyToolbar(
            navigator = navigator,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 57.dp, start = 15.dp, end = 15.dp),
            showBack = currentPage == 0,
            showClose = currentPage > 0
        )
        StartSurveyBtn(
            onStartSurvey = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 54.dp, end = 20.dp)
                .semantics { contentDescription = startSurveyDescription }
        )
    }
}

@Composable
fun StartSurveyBtn(onStartSurvey: () -> Unit, modifier: Modifier = Modifier) {
    var showStartSurveyBtn by remember { mutableStateOf(true) }

    Crossfade(
        targetState = showStartSurveyBtn,
        animationSpec = tween(TWEEN_ANIM_TIME),
        modifier = modifier
    ) {
        if (it) {
            Button(
                onClick = {
                    onStartSurvey()
                    showStartSurveyBtn = false
                },
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
fun SurveyDetailStartScreen(survey: Survey, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = survey.title,
            fontFamily = NeuzeitFamily,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 28.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = survey.description,
            fontFamily = NeuzeitFamily,
            fontWeight = FontWeight.Normal,
            color = White70,
            fontSize = 17.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun SurveyToolbar(navigator: DestinationsNavigator, modifier: Modifier, showBack: Boolean, showClose: Boolean) {
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
                onClick = {
                    // TODO: Implement dialog to confirm close
                },
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
