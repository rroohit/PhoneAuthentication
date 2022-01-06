package com.phoneauthentication.presentation.login

import android.app.Activity
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.phoneauthentication.R
import com.phoneauthentication.domain.components.CountryCodeDialog
import com.phoneauthentication.util.Screen
import com.phoneauthentication.util.UiEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@ExperimentalAnimationApi
@Composable
fun LoginScreen(
    scaffoldState: ScaffoldState,
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp

    val activity = LocalContext.current as Activity

    val composableScope = rememberCoroutineScope()

    var logoState by rememberSaveable {
        mutableStateOf(LogoPosition.Start)
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.heart_logo)
    )

    val progress by animateLottieCompositionAsState(
        composition,
        clipSpec = LottieClipSpec.Progress(0f, 0.55f)
    )

    val offsetAnimation: Dp by animateDpAsState(
        targetValue =
        if (logoState == LogoPosition.Start) 5.dp else 280.dp,
        spring(stiffness = Spring.StiffnessVeryLow)
    )

    var uiState by rememberSaveable {
        mutableStateOf(UiVisible.LoginButton)
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Initial -> Unit

                is UiEvent.NavigatePhoneNumberUi -> uiState = UiVisible.PhoneNumber

                is UiEvent.NavigateOtpUi -> uiState = UiVisible.VerifyOtp

                is UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.uiText,
                    )
                }

                is UiEvent.UserLoggedIn -> {
                    navController.popBackStack(
                        route = Screen.LoginScreen.route,
                        inclusive = true
                    )
                    navController.navigate(route = Screen.HomeScreen.route)
                }

                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .absoluteOffset(y = -offsetAnimation),
        contentAlignment = Alignment.Center
    ) {

        LottieAnimation(
            composition,
            progress,
            modifier = Modifier.size(200.dp, 200.dp)
                .semantics {
                           testTag = "logo"
                },

        )
        composableScope.launch {
            delay(1200)
            logoState = LogoPosition.Finish

        }


    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Box(modifier = Modifier.height(screenHeight / 2)) {}

        AnimatedVisibility(
            visible = logoState == LogoPosition.Finish,

            ) {
            Box(
                modifier = Modifier.height(screenHeight / 2),
                contentAlignment = Alignment.TopCenter
            ) {
                if (logoState == LogoPosition.Finish) {
                    when (uiState) {
                        UiVisible.LoginButton -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "")

                                Spacer(modifier = Modifier.height(112.dp))

                                Button(
                                    onClick = {
                                        viewModel.onEvent(UiEvent.NavigatePhoneNumberUi)

                                    },
                                    modifier = Modifier.size(200.dp, 50.dp)
                                        .semantics {
                                            testTag = "login button"
                                        }
                                ) {
                                    Text(text = "Login")
                                }
                            }

                        }
                        UiVisible.PhoneNumber -> {
                            Column(
                                modifier = Modifier
                                    .padding(32.dp, 0.dp, 32.dp, 0.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = stringResource(id = R.string.enter_mobile_num))

                                Row(
                                    modifier = Modifier.height(100.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceAround

                                ) {

                                    CountryCodeDialog()

                                    Spacer(modifier = Modifier.width(16.dp))

                                    OutlinedTextField(
                                        value = viewModel.phoneNumber.value,
                                        onValueChange = { phoneNo ->
                                            if (phoneNo.length <= 10) {
                                                viewModel.setPhoneNumberText(phoneNo.filter { str ->
                                                    !str.isWhitespace()
                                                })
                                            }
                                        },
                                        placeholder = {
                                            Text(
                                                text = "99750 12345",
                                                style = MaterialTheme.typography.body1
                                            )
                                        },
                                        textStyle = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            letterSpacing = 2.sp
                                        ),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Phone
                                        ),
                                        modifier = Modifier.semantics {
                                            testTag = "input phone number"
                                        }
                                    )


                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                when (viewModel.loginLoading.value) {
                                    true -> {
                                        AnimatedVisibility(
                                            visible = viewModel.loginLoading.value,
                                            enter = fadeIn(),
                                            exit = fadeOut()
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(50.dp, 50.dp),
                                            )


                                        }

                                    }
                                    false -> {
                                        AnimatedVisibility(
                                            visible = !viewModel.loginLoading.value,
                                            enter = fadeIn(),
                                            exit = fadeOut()
                                        ) {
                                            Button(
                                                modifier = Modifier.size(200.dp, 50.dp)
                                                    .semantics {
                                                               testTag  = "next button"
                                                    },
                                                onClick = {

                                                    if (viewModel.phoneNumber.value.length < 10) {

                                                        viewModel.onEvent(
                                                            UiEvent.ShowSnackBar(
                                                                "Enter Valid Phone Number"
                                                            )
                                                        )


                                                    } else {

                                                        viewModel.onEvent(
                                                            UiEvent.PhoneNumberUiButtonClick(
                                                                activity = activity
                                                            )
                                                        )

                                                    }
                                                }

                                            ) {
                                                Text(text = stringResource(id = R.string.next))
                                            }

                                        }

                                    }

                                }
                            }

                        }
                        UiVisible.VerifyOtp -> {
                            Box(
                                modifier = Modifier.fillMaxHeight()
                                    .padding(bottom = 24.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(12.dp, 0.dp, 12.dp, 0.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {

                                    Text(text = stringResource(id = R.string.enter_otp))

                                    Row(
                                        Modifier
                                            .height(100.dp)
                                            .padding(start = 100.dp, end = 100.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {

                                        TextField(
                                            value = viewModel.otp.value,
                                            onValueChange = { otp ->
                                                if (otp.length <= 6) {
                                                    viewModel.setOtpText(otp.filter { str ->
                                                        !str.isWhitespace()
                                                    })
                                                }

                                            },
                                            maxLines = 1,
                                            textStyle = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 20.sp,
                                                textAlign = TextAlign.Center,
                                                letterSpacing = 10.sp
                                            ),
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType = KeyboardType.Number
                                            ),
                                            colors = TextFieldDefaults.textFieldColors(
                                                backgroundColor = Color.Transparent,
                                            ),
                                            modifier = Modifier.semantics {
                                                testTag = "input otp"
                                            }

                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    when (viewModel.loginLoading.value) {
                                        true -> {

                                            AnimatedVisibility(
                                                visible = viewModel.loginLoading.value,
                                                enter = fadeIn(),
                                                exit = fadeOut()
                                            ) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(50.dp, 50.dp),
                                                )


                                            }


                                        }
                                        false -> {
                                            Button(
                                                modifier = Modifier.size(200.dp, 50.dp),
                                                onClick = {
                                                    val otp = viewModel.otp.value

                                                    if (otp.length < 6) {
                                                        viewModel.onEvent(
                                                            UiEvent.ShowSnackBar(
                                                                "Enter Valid Otp"
                                                            )
                                                        )
                                                    } else {
                                                        viewModel.onEvent(
                                                            UiEvent.VerifyOtpUiButtonClick
                                                        )

                                                    }
                                                }

                                            ) {
                                                Text(text = stringResource(id = R.string.verify))
                                            }

                                            Spacer(modifier = Modifier.height(16.dp))


                                            Text(
                                                text = viewModel.countDownTime.value,
                                                modifier = Modifier
                                                    .clickable {
                                                        if (viewModel.countDownTime.value == "Resend Code") {
                                                            viewModel.onEvent(
                                                                UiEvent.OnResendOtpClick(
                                                                    activity = activity
                                                                )
                                                            )

                                                        }

                                                    }

                                            )
                                        }
                                    }






                                }

                                Text(
                                    text = buildAnnotatedString {
                                        val phoneText = viewModel.phoneNumber.value
                                        withStyle(
                                            style = SpanStyle(
                                                color = MaterialTheme.colors.primary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 17.sp
                                            )
                                        ) {
                                            append(phoneText)
                                        }
                                        append(" ")
                                        append(stringResource(id = R.string.wrong_num_click_here))

                                    },
                                    style = MaterialTheme.typography.body1,
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .clickable {
                                            if (!viewModel.loginLoading.value) {
                                                viewModel.setPhoneNumberText("")
                                                viewModel.onEvent(UiEvent.NavigatePhoneNumberUi)
                                            }

                                        }

                                )
                            }


                        }

                    }

                }

            }

        }

    }

}


enum class UiVisible {
    LoginButton,
    PhoneNumber,
    VerifyOtp,
}

enum class LogoPosition {
    Start,
    Finish,
}