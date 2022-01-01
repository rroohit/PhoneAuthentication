package com.phoneauthentication.domain.repository

import com.google.firebase.auth.PhoneAuthProvider

interface PhoneCallbacksListener {
    fun onOtpVerifyCompleted()
    fun onOtpVerifyFailed(message: String)
    fun onVerificationCodeDetected(otpCode: String)
    fun onVerificationFailed(message: String)
    fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken?)

}