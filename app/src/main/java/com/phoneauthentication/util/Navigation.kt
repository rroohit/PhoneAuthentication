package com.phoneauthentication.util

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.phoneauthentication.presentation.home.HomeScreen
import com.phoneauthentication.presentation.login.LoginScreen

@ExperimentalAnimationApi
@Composable
fun Navigation(
    scaffoldState: ScaffoldState
) {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route
    ) {

        composable(Screen.LoginScreen.route){
            LoginScreen(
                scaffoldState = scaffoldState,
                navController = navController
            )
        }

        composable(Screen.HomeScreen.route){
            HomeScreen()
        }


    }


}