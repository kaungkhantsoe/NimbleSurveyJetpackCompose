package com.kks.nimblesurveyjetpackcompose.ui.presentation.login

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kks.nimblesurveyjetpackcompose.viewmodel.login.LoginViewModel
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    val activity = (LocalContext.current as? Activity)

    viewModel.login()
    BackHandler {
        activity?.finish()
    }
}
