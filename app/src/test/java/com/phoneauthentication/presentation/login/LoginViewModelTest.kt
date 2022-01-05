package com.phoneauthentication.presentation.login


import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        viewModel = LoginViewModel(FakeLoginRepository())
    }

    @Test
    fun verifyOtp() {

        assertEquals(3, 6 - 3)
    }

    @Test
    fun checkIsUserLoggedInTrue() {

    }

    @Test
    fun checkIsUserLoggedInFalse() {

    }

}