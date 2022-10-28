@file:Suppress("TooManyFunctions")
package com.kks.nimblesurveyjetpackcompose.ui.presentation.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.model.ErrorModel
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.ErrorAlertDialog
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.Loading
import com.kks.nimblesurveyjetpackcompose.ui.presentation.destinations.HomeScreenDestination
import com.kks.nimblesurveyjetpackcompose.ui.theme.Concord
import com.kks.nimblesurveyjetpackcompose.ui.theme.CornerRadius
import com.kks.nimblesurveyjetpackcompose.ui.theme.White18
import com.kks.nimblesurveyjetpackcompose.util.TWEEN_ANIM_TIME
import com.kks.nimblesurveyjetpackcompose.viewmodel.splash.SplashUiState
import com.kks.nimblesurveyjetpackcompose.viewmodel.splash.SplashUiStatePreviewParameterProvider
import com.kks.nimblesurveyjetpackcompose.viewmodel.splash.SplashViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavHostParam
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

private const val SPLASH_TIME = 2000L

@RootNavGraph(start = true)
@Destination
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator,
    @NavHostParam splashTime: Long = SPLASH_TIME,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val splashUiState by viewModel.splashUiState.collectAsState()

    if (splashUiState.isLoginSuccess) {
        navigator.navigate(HomeScreenDestination)
    }

    SplashScreenContent(
        shouldShowLoginComponents = splashUiState.shouldNavigateToLogin,
        shouldShowLoading = splashUiState.shouldShowLoading,
        error = splashUiState.error,
        onClickLogin = { email, password ->
            viewModel.login(email = email, password = password)
        }
    ) {
        viewModel.resetError()
    }

    viewModel.startTimerToNavigateToLogin(splashTime = splashTime)
}

@Composable
fun SplashScreenContent(
    shouldShowLoginComponents: Boolean,
    shouldShowLoading: Boolean,
    error: ErrorModel?,
    onClickLogin: (email: String, password: String) -> Unit,
    onCloseErrorDialog: () -> Unit
) {
    val logoOffset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
    val positionToAnimate = -LocalDensity.current.run { 221.dp.toPx() }

    LaunchedEffect(key1 = shouldShowLoginComponents) {
        if (shouldShowLoginComponents) {
            launch {
                logoOffset.animateTo(
                    Offset(logoOffset.value.x, positionToAnimate),
                    spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = StiffnessVeryLow
                    )
                )
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        BackgroundImage(isBlurred = shouldShowLoginComponents)
        LogoImage(
            modifier = Modifier
                .size(201.0.dp, 48.0.dp)
                .offset {
                    IntOffset(
                        x = logoOffset.value.x.toInt(),
                        y = logoOffset.value.y.toInt()
                    )
                }
        )
        AnimatedVisibility(visible = shouldShowLoginComponents, enter = fadeIn()) { LoginComponents(onClickLogin) }
        if (shouldShowLoading) Loading()
        error?.let { error ->
            ErrorAlertDialog(
                errorModel = error,
                title = stringResource(id = R.string.oops),
                buttonText = stringResource(id = android.R.string.ok),
                onClickButton = onCloseErrorDialog
            )
        }
    }
}

@Composable
fun BackgroundImage(isBlurred: Boolean) {
    Crossfade(
        targetState = isBlurred,
        animationSpec = tween(TWEEN_ANIM_TIME)
    ) {
        Image(
            painter = painterResource(id = if (it) R.drawable.ic_overlay else R.drawable.splash_bg),
            contentDescription = stringResource(id = R.string.splash_background_content_description),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun LogoImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_logo_white),
        contentDescription = stringResource(id = R.string.splash_logo_content_description),
        modifier = modifier
    )
}

@Composable
fun LoginComponents(onClickLogin: (email: String, password: String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        var enableLoginButton by remember { mutableStateOf(false) }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        EmailTextField(
            emailState = email,
            onValueChange = {
                email = it
                enableLoginButton = email.isNotEmpty() && password.isNotEmpty()
            }
        )
        PasswordTextField(
            passwordState = password,
            onValueChange = {
                password = it
                enableLoginButton = email.isNotEmpty() && password.isNotEmpty()
            }
        )
        LoginButton(loginButtonState = enableLoginButton) { onClickLogin(email, password) }
    }
}

@Composable
fun EmailTextField(emailState: String, onValueChange: (String) -> Unit) {
    val focusManager = LocalFocusManager.current
    val emailContentDescription = stringResource(id = R.string.login_email_text_field)

    TextField(
        value = emailState,
        onValueChange = { onValueChange(it) },
        shape = RoundedCornerShape(CornerRadius),
        singleLine = true,
        label = { Text(stringResource(R.string.login_email)) },
        colors = textFieldColor(),
        modifier = Modifier
            .loginTextFieldModifier()
            .semantics { contentDescription = emailContentDescription },
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        )
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PasswordTextField(passwordState: String, onValueChange: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val passwordContentDescriptorDetachedException =
        stringResource(id = R.string.login_password_text_field)

    TextField(
        value = passwordState,
        onValueChange = { onValueChange(it) },
        shape = RoundedCornerShape(CornerRadius),
        singleLine = true,
        label = { Text(stringResource(id = R.string.login_password)) },
        colors = textFieldColor(),
        modifier = Modifier
            .loginTextFieldModifier()
            .semantics { contentDescription = passwordContentDescriptorDetachedException },
        trailingIcon = {
            TextButton(onClick = {
                // TODO: Implement forgot password function
            }) {
                Text(stringResource(id = R.string.login_forget), color = Color.White)
            }
        },
        visualTransformation = PasswordVisualTransformation(),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        )
    )
}

@Composable
fun LoginButton(loginButtonState: Boolean, onClickLogin: () -> Unit) {
    val loginButtonContentDescription = stringResource(id = R.string.login_log_in_button)
    Button(
        onClick = onClickLogin,
        shape = RoundedCornerShape(CornerRadius),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        modifier = Modifier
            .loginTextFieldModifier()
            .semantics { contentDescription = loginButtonContentDescription },
        enabled = loginButtonState
    ) {
        Text(
            stringResource(R.string.login_log_in),
            color = Color.Black,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun textFieldColor() =
    TextFieldDefaults.textFieldColors(
        backgroundColor = White18,
        cursorColor = Color.Black,
        disabledLabelColor = Concord,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        focusedLabelColor = Concord,
        unfocusedLabelColor = Concord,
        textColor = Color.White
    )

private fun Modifier.loginTextFieldModifier() = this
    .fillMaxWidth()
    .padding(horizontal = 24.dp)
    .height(56.dp)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashPreview(
    @PreviewParameter(SplashUiStatePreviewParameterProvider::class) splashUiState: SplashUiState
) {
    SplashScreenContent(
        shouldShowLoginComponents = splashUiState.shouldNavigateToLogin,
        shouldShowLoading = splashUiState.shouldShowLoading,
        error = splashUiState.error,
        onClickLogin = { _, _ -> }
    ) {}
}
