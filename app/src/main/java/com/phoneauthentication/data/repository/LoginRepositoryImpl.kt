package com.phoneauthentication.data.repository

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.phoneauthentication.domain.repository.LoginRepository
import com.phoneauthentication.domain.repository.PhoneCallbacksListener
import java.util.concurrent.TimeUnit

class LoginRepositoryImpl
constructor(
    private val auth: FirebaseAuth
) : LoginRepository {

    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    lateinit var verificationID: String

    private lateinit var phoneCallbacksListener: PhoneCallbacksListener

    override fun setPhoneCallbacksListener(listener: PhoneCallbacksListener) {
        this.phoneCallbacksListener = listener

    }


    private val callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                val otpCode = credential.smsCode

                if (!otpCode.isNullOrEmpty()) {
                    phoneCallbacksListener.onVerificationCodeDetected(otpCode = otpCode)
                }

            }

            override fun onVerificationFailed(firebaseException: FirebaseException) {
                println("Firebase exception => ${firebaseException.message}")
                when (firebaseException) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        phoneCallbacksListener.onVerificationFailed("Invalid Phone Number")
                    }

                    is FirebaseTooManyRequestsException -> {
                        phoneCallbacksListener.onVerificationFailed("Too many attempts. Retry later.")
                    }

                    is FirebaseNetworkException -> {
                        phoneCallbacksListener.onVerificationFailed("Network not available.")

                    }

                    else -> {
                        phoneCallbacksListener.onVerificationFailed(firebaseException.message ?: "")
                        // phoneCallbacksListener.onVerificationFailed("Unknown Error try later.")
                    }
                }
            }

            override fun onCodeSent(
                verificationId: String,
                _resendToken: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, _resendToken)
                verificationID = verificationId
                resendToken = _resendToken

                phoneCallbacksListener.onCodeSent(verificationId, resendToken)
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
            }


        }


    override suspend fun sendOtpToPhone(phoneNumber: String, activity: Activity){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun verifyOtpCode(otpCode: String) {
        signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(verificationID, otpCode))
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                phoneCallbacksListener.onOtpVerifyCompleted()
            } else {
                when(it.exception){
                    is FirebaseAuthInvalidCredentialsException -> {
                        phoneCallbacksListener.onOtpVerifyFailed("Wrong Otp")
                    }
                }
            }
        }
    }

    override suspend fun resendOtpCode(phoneNumber: String, activity: Activity) {
        val resendCodeOptionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
        resendCodeOptionsBuilder.setForceResendingToken(resendToken)
        PhoneAuthProvider.verifyPhoneNumber(resendCodeOptionsBuilder.build())
    }

    override suspend fun isUserVerified(): Boolean = auth.currentUser != null

    override suspend fun setVerificationId(verificationId: String) {
        verificationID = verificationId
    }



}