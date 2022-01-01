package com.phoneauthentication.domain.repository

import android.app.Activity

interface LoginRepository {

    fun setPhoneCallbacksListener(listener : PhoneCallbacksListener)

    suspend fun sendOtpToPhone(phoneNumber: String, activity: Activity)

    suspend fun resendOtpCode(phoneNumber: String, activity: Activity)

    suspend fun isUserVerified(): Boolean

    suspend fun setVerificationId(verificationId: String)

    fun verifyOtpCode(otpCode: String)
}