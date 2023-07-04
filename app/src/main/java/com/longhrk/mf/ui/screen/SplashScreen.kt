package com.longhrk.mf.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.longhrk.mf.R
import com.longhrk.mf.ui.theme.splashColor
import com.longhrk.mf.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    authViewModel: AuthViewModel,
    onLoginScreen: () -> Unit,
    onHomeScreen: () -> Unit,
) {
    val isSkipLogin by authViewModel.isLogin.collectAsState()
    val userLocal by authViewModel.currentUserInLocal.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.getUserInLocalStorage().await()
        userLocal?.let {
            authViewModel.isSkipLogin(it.email).await()
            if (isSkipLogin)
                onHomeScreen()
            else
                onLoginScreen()
        } ?: run {
            delay(1000)
            onLoginScreen()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(splashColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.circle_logo),
            contentDescription = "Logo"
        )
    }
}