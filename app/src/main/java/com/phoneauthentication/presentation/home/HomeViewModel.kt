package com.phoneauthentication.presentation.home

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val auth: FirebaseAuth
): ViewModel() {

    fun logOut(){
        auth.signOut()
    }

}