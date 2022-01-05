package com.phoneauthentication.presentation.login

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ScaffoldState
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.phoneauthentication.presentation.main.MainActivity
import com.phoneauthentication.presentation.main.ui.theme.PhoneAuthenticationTheme
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@ExperimentalAnimationApi
@RunWith(AndroidJUnit4::class)
class LoginScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @RelaxedMockK
    lateinit var navController: NavController

    @RelaxedMockK
    lateinit var scaffoldState: ScaffoldState

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }


    @Test
    fun splashScreen_LogoDisplays() = testDispatcher.runBlockingTest {
        composeTestRule.setContent {
            PhoneAuthenticationTheme {
                LoginScreen(
                    navController = navController,
                    scaffoldState = scaffoldState
                )
            }
        }

        composeTestRule
            .onNodeWithTag("logo")
            .assertExists()

        advanceTimeBy(1200)

        composeTestRule
            .onNodeWithTag("logo")
            .assertExists()
    }



}