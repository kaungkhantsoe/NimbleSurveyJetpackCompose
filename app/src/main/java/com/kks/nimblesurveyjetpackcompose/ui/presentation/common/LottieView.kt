package com.kks.nimblesurveyjetpackcompose.ui.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieRetrySignal
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.ui.theme.NeuzeitFamily

private const val SUBMIT_SUCCESS_LOTTIE_URL = "https://assets2.lottiefiles.com/packages/lf20_pmYw5P.json"
private const val LOTTIE_ENDS = 1.0f
private const val LOTTIE_FAIL_COUNT = 3

@Composable
fun LottieView(onLottieEnds: () -> Unit) {
    val retrySignal = rememberLottieRetrySignal()
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Url(SUBMIT_SUCCESS_LOTTIE_URL),
        onRetry = { failCount, _ ->
            retrySignal.awaitRetry()
            failCount < LOTTIE_FAIL_COUNT
        }
    )
    val progress by animateLottieCompositionAsState(composition)

    if (progress == LOTTIE_ENDS) onLottieEnds()

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
