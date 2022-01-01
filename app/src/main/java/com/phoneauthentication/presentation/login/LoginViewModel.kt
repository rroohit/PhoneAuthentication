package com.phoneauthentication.presentation.login

import android.app.Activity
import android.os.CountDownTimer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthProvider
import com.phoneauthentication.domain.repository.LoginRepository
import com.phoneauthentication.domain.repository.PhoneCallbacksListener
import com.phoneauthentication.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val repository: LoginRepository
) : ViewModel(), PhoneCallbacksListener {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    private val _phoneNumber = mutableStateOf("")
    val phoneNumber: State<String> = _phoneNumber


    private val _otp = mutableStateOf("")
    val otp: State<String> = _otp


    private val _loginLoading = mutableStateOf(false)
    val loginLoading: State<Boolean> = _loginLoading

    private val _countDownTime = mutableStateOf("")
    val countDownTime: State<String> = _countDownTime

    private var timer: CountDownTimer? = null


    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.Initial -> Unit
            is UiEvent.OnLoginButtonClick -> setUiEvent(UiEvent.NavigatePhoneNumberUi)

            is UiEvent.NavigatePhoneNumberUi -> Unit
            is UiEvent.PhoneNumberUiButtonClick -> {
//                viewModelScope.launch {
//                    sendOtp(phoneNumber.value, event.activity)
//                }
            }

            is UiEvent.NavigateOtpUi -> Unit
            is UiEvent.VerifyOtpUiButtonClick -> {
                //verifyOtpCode(otpCode = otp.value)
            }

            is UiEvent.OnResendOtpClick -> {
//                viewModelScope.launch {
//                    resendOtp(phoneNumber.value, event.activity)
//                }
            }


            is UiEvent.ShowSnackBar -> {
                setUiEvent(UiEvent.ShowSnackBar(event.uiText))
            }

            is UiEvent.UserLoggedIn -> Unit
        }
    }

    fun setUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    fun setPhoneNumberText(phoneNo: String) {
        _phoneNumber.value = phoneNo
    }


    fun setOtpText(otp: String) {
        _otp.value = otp
    }

    private fun startCountDown() {
        val startTime = 60
        timer?.cancel()
        timer = object : CountDownTimer(startTime * 1000.toLong(), 1000) {
            override fun onTick(p0: Long) {
                _countDownTime.value = (p0 / 1000).toInt().toString()
            }

            override fun onFinish() {
                _countDownTime.value = "Resend Code"
            }

        }
        timer?.start()

    }

    private suspend fun sendOtp(phoneNo: String, activity: Activity) {
        viewModelScope.launch {
            _loginLoading.value = true
            repository.sendOtpToPhone(phoneNo, activity)
        }

    }

    private suspend fun resendOtp(phoneNo: String, activity: Activity) {
        _loginLoading.value = true
        repository.resendOtpCode(phoneNo, activity)
    }

    private fun verifyOtpCode(otpCode: String) {
        _loginLoading.value = true
        repository.verifyOtpCode(otpCode = otpCode)
    }


    override fun onOtpVerifyCompleted() {
        viewModelScope.launch {
            _uiEvent.send(
                UiEvent.UserLoggedIn
            )
        }
    }

    override fun onOtpVerifyFailed(message: String) {
        _loginLoading.value = false
        viewModelScope.launch {
            _uiEvent.send(
                UiEvent.ShowSnackBar(
                    message
                )
            )
        }
    }

    override fun onVerificationCodeDetected(otpCode: String) {
        _otp.value = otpCode
    }

    override fun onVerificationFailed(message: String) {
        _loginLoading.value = false
        viewModelScope.launch {
            _uiEvent.send(
                UiEvent.ShowSnackBar(
                    message
                )
            )
        }
    }

    override fun onCodeSent(
        verificationId: String?,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        startCountDown()
        _loginLoading.value = false
        viewModelScope.launch {
            _uiEvent.send(UiEvent.NavigateOtpUi)


        }
    }

}