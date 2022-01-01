package com.phoneauthentication.presentation.login

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.google.common.base.Verify.verify
import com.phoneauthentication.R
import com.phoneauthentication.domain.components.CountryCodeDialog
import com.phoneauthentication.domain.components.PhoneNumTextField
import com.phoneauthentication.util.Screen
import com.phoneauthentication.util.UiEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.temporal.TemporalAdjusters.next

@ExperimentalAnimationApi
@Composable
fun LoginScreen(
    scaffoldState: ScaffoldState,
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {

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
                is UiEvent.OnLoginButtonClick -> uiState = UiVisible.PhoneNumber

                is UiEvent.NavigatePhoneNumberUi -> uiState = UiVisible.PhoneNumber
                is UiEvent.PhoneNumberUiButtonClick -> {

                }

                is UiEvent.NavigateOtpUi -> uiState = UiVisible.VerifyOtp
                is UiEvent.VerifyOtpUiButtonClick -> {

                }
                is UiEvent.OnResendOtpClick -> {

                }

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

        Box {}

        AnimatedVisibility(
            visible = logoState == LogoPosition.Finish,

            ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                if (logoState == LogoPosition.Finish) {
                    when (uiState) {
                        UiVisible.LoginButton -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "")

                                Spacer(modifier = Modifier.height(116.dp))


                                Button(
                                    onClick = {
                                        viewModel.setUiEvent(UiEvent.NavigatePhoneNumberUi)

                                    },
                                    modifier = Modifier.size(200.dp, 50.dp)
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


                                    PhoneNumTextField(
                                        text = viewModel.phoneNumber.value,
                                        hint = "99750 64095",
                                        maxLength = 10,
                                        onTextChange = { phoneNo ->
                                            viewModel.setPhoneNumberText(phoneNo = phoneNo)
                                        },
                                        keyboardType = KeyboardType.Phone,
                                        textStyle = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            letterSpacing = 2.sp
                                        )
                                    )

                                }

                                Spacer(modifier = Modifier.height(16.dp))

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
                                                modifier = Modifier.size(200.dp, 50.dp),
                                                onClick = {
                                                    viewModel.setUiEvent(UiEvent.NavigateOtpUi)
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
                            Column(
                                modifier = Modifier
                                    .padding(56.dp, 0.dp, 56.dp, 0.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Spacer(modifier = Modifier.height(50.dp))

                                Text(text = stringResource(id = R.string.enter_otp))

                                Row(
                                    Modifier
                                        .height(116.dp)
                                        .padding(start = 50.dp, end = 50.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    OutlinedTextField(
                                        value = viewModel.otp.value,
                                        onValueChange = { otp ->
                                            if (otp.length <= 6) {
                                                viewModel.setOtpText(otp)

                                            }
                                        },
                                        textStyle = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            textAlign = TextAlign.Center,
                                            letterSpacing = 10.sp
                                        ),
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number
                                        ),
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

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
                                                viewModel.setUiEvent(UiEvent.UserLoggedIn)
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
                                                        viewModel.setUiEvent(
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

                        }

                    }

                }

            }

        }

    }

}

//
//
//@ExperimentalAnimationApi
//@Composable
//fun OtpUi(
//    viewModel: LoginViewModel,
//    activity: Activity,
//    onButtonClick: () -> Unit
//) {
//
//    Box() {
//
//        Column(
//            modifier = Modifier
//                .padding(24.dp, 0.dp, 24.dp, 0.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//
//        }
//
//        Text(
//            text = buildAnnotatedString {
//                val signUpText = viewModel.phoneNumber.value
//                withStyle(
//                    style = SpanStyle(
//                        color = MaterialTheme.colors.primary
//                    )
//                ) {
//                    append(signUpText)
//                }
//                append(" ")
//                append(stringResource(id = R.string.wrong_num_click_here))
//
//            },
//            style = MaterialTheme.typography.body1,
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .clickable {
//                    if (!viewModel.loginLoading.value) {
//                        viewModel.setPhoneNumberText("")
//                        viewModel.setUiEvent(UiEvent.NavigatePhoneNumberUi)
//                    }
//
//                }
//
//        )
//
//    }
//
//}

enum class UiVisible {
    LoginButton,
    PhoneNumber,
    VerifyOtp,
}

enum class LogoPosition {
    Start,
    Finish,
}