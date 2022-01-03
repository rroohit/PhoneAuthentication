package com.phoneauthentication.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.phoneauthentication.util.Screen

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Home",
            style = TextStyle(
                fontSize = 19.sp,
                textAlign = TextAlign.Center,
            )
        )

        Spacer(modifier = Modifier.height(100.dp))

        Text(
            text = "Log Out",
            modifier = Modifier
                .clickable {
                    viewModel.logOut()
                    navController.popBackStack(
                        route = Screen.HomeScreen.route,
                        inclusive = true
                    )
                    navController.navigate(route = Screen.LoginScreen.route)
                },
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 21.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
            )


        )


    }


}