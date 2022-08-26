package com.kks.nimblesurveyjetpackcompose.ui.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.viewmodel.splash.SplashViewModel
import com.ramcosta.composedestinations.annotation.Destination

@Destination(start = true)
@Composable
fun SplashScreen(viewModel: SplashViewModel = viewModel()) {
    LaunchedEffect(
        key1 = viewModel.shouldNavigateToLogin.value
    ) {
        if (viewModel.shouldNavigateToLogin.value) {
            // TODO: Navigate to Login
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_bg),
            contentDescription = stringResource(id = R.string.splash_background_content_description),
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )
        Image(
            painter = painterResource(id = R.drawable.ic_logo_white),
            contentDescription = stringResource(id = R.string.splash_logo_content_description),
            modifier = Modifier.size(201.0.dp, 48.0.dp)
        )
    }
    viewModel.startTimerToNavigateToLogin()
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    SplashScreen()
}
