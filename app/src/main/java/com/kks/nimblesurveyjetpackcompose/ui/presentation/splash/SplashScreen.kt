package com.kks.nimblesurveyjetpackcompose.ui.presentation.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kks.nimblesurveyjetpackcompose.R
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.ErrorAlertDialog
import com.kks.nimblesurveyjetpackcompose.ui.presentation.common.Loading
import com.kks.nimblesurveyjetpackcompose.ui.presentation.destinations.HomeScreenDestination
import com.kks.nimblesurveyjetpackcompose.ui.theme.Concord
import com.kks.nimblesurveyjetpackcompose.ui.theme.White18
import com.kks.nimblesurveyjetpackcompose.util.extensions.ErrorType
import com.kks.nimblesurveyjetpackcompose.util.extensions.loginTextFieldModifier
import com.kks.nimblesurveyjetpackcompose.viewmodel.splash.SplashViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.launch
import com.ramcosta.composedestinations.annotation.NavHostParam
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph(start = true)
@Destination
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator,
    @NavHostParam splashTime: Long = 2000L,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val showLoginComponents by viewModel.shouldNavigateToLogin.collectAsState()
    var shouldShowLoading by remember { mutableStateOf(false) }
    var shouldShowError by remember { mutableStateOf(ErrorType.NONE to "") }

    val logoOffset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
    val positionToAnimate = -LocalDensity.current.run { 221.dp.toPx() }
    val context = LocalContext.current

    LaunchedEffect(
        keys = arrayOf(
            viewModel.showLoginComponents,
            viewModel.shouldShowLoading(),
            viewModel.isError(),
            viewModel.isLoginSuccess(),
        )
    ) {
        if (showLoginComponents) {
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

        shouldShowLoading = viewModel.shouldShowLoading()
        shouldShowError = viewModel.isError()
        if (viewModel.isLoginSuccess()) navigator.navigate(HomeScreenDestination)
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = if (showLoginComponents) R.drawable.ic_overlay else R.drawable.splash_bg),
            contentDescription = stringResource(id = R.string.splash_background_content_description),
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )
        Image(
            painter = painterResource(id = R.drawable.ic_logo_white),
            contentDescription = stringResource(id = R.string.splash_logo_content_description),
            modifier = Modifier
                .size(201.0.dp, 48.0.dp)
                .offset {
                    IntOffset(
                        x = logoOffset.value.x.toInt(),
                        y = logoOffset.value.y.toInt()
                    )
                }
        )
        AnimatedVisibility(visible = showLoginComponents, enter = fadeIn()) {
            LoginComponents()
        }
        Loading(showLoading = shouldShowLoading)
        ErrorAlertDialog(
            errorState = shouldShowError,
            title = context.getString(R.string.oops),
            buttonText = context.getString(android.R.string.ok),
            onClickButton = { viewModel.resetError() }
        )
    }
    viewModel.startTimerToNavigateToLogin(splashTime = splashTime)
}

@Composable
fun LoginComponents(viewModel: SplashViewModel = hiltViewModel()) {
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
        LoginButton(loginButtonState = enableLoginButton) {
            viewModel.login(email = email, password = password)
        }
    }
}

@Composable
fun EmailTextField(emailState: String, onValueChange: (String) -> Unit) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = emailState,
        onValueChange = { onValueChange(it) },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        label = { Text(LocalContext.current.getString(R.string.login_email)) },
        colors = textFieldColor(),
        modifier = Modifier.loginTextFieldModifier(),
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
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = passwordState,
        onValueChange = { onValueChange(it) },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        label = { Text(context.getString(R.string.login_password)) },
        colors = textFieldColor(),
        modifier = Modifier.loginTextFieldModifier(),
        trailingIcon = {
            TextButton(onClick = {
                // TODO: Implement forgot password function
            }) {
                Text(context.getString(R.string.login_forget), color = Color.White)
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
    Button(
        onClick = { onClickLogin() },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        modifier = Modifier.loginTextFieldModifier(),
        enabled = loginButtonState
    ) {
        Text(
            LocalContext.current.getString(R.string.login_log_in),
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
    .padding(start = 24.dp, end = 24.dp)
    .height(56.dp)

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    SplashScreen(EmptyDestinationsNavigator)
}
