package com.phoneauthentication.presentation.login

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ScaffoldState
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
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
    fun splashScreen_LoginScreen_Components() = testDispatcher.runBlockingTest {
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
            .assertIsDisplayed()

        advanceTimeBy(1200)

        composeTestRule
            .onNodeWithTag("login button")
            .assertExists()
            .assertIsDisplayed()
            .performClick()


        composeTestRule
            .onNodeWithTag("input phone number")
            .assertExists()
            .assertIsDisplayed()
            .performTextClearance()

        composeTestRule
            .onNodeWithTag("input phone number")
            .performTextInput("9975064095")

        composeTestRule
            .onNodeWithTag("input phone number")
            .performTextInput("00")

        composeTestRule
            .onNodeWithTag("input phone number")
            .assertTextEquals("9975064095")

        composeTestRule
            .onNodeWithTag("next button")
            .assertExists()
            .assertIsDisplayed()
            .performClick()


        //Need to navigate to otp layout
        composeTestRule
            .onNodeWithTag("input otp")
            .assertExists()



    }




}