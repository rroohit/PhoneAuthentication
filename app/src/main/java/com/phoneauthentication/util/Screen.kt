package com.phoneauthentication.util

sealed class Screen(val route: String) {
    object LoginScreen: Screen("login_screen")
    object HomeScreen: Screen("home_screen")

}