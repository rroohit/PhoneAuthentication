package com.phoneauthentication.presentation.login

import android.app.Activity
import com.phoneauthentication.domain.repository.LoginRepository
import com.phoneauthentication.domain.repository.PhoneCallbacksListener

class FakeLoginRepository : LoginRepository{

    lateinit var verificationID: String

    private lateinit var phoneCallbacksListener: PhoneCallbacksListener



    override fun setPhoneCallbacksListener(listener: PhoneCallbacksListener) {
        this.phoneCallbacksListener = listener
    }

    override suspend fun sendOtpToPhone(phoneNumber: String, activity: Activity) {
        TODO("Not yet implemented")
    }

    override suspend fun resendOtpCode(phoneNumber: String, activity: Activity) {
        TODO("Not yet implemented")
    }

    override suspend fun isUserVerified(): Boolean {
        return true
    }

    override suspend fun setVerificationId(verificationId: String) {
        verificationID = verificationId
    }

    override fun verifyOtpCode(otpCode: String) {
        TODO("Not yet implemented")
    }
}