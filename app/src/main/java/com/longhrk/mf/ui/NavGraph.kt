package com.longhrk.mf.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.longhrk.mf.ui.event.NavEvent
import com.longhrk.mf.ui.screen.HomeScreen
import com.longhrk.mf.ui.screen.LoginScreen
import com.longhrk.mf.ui.screen.OtherScreen
import com.longhrk.mf.ui.screen.SplashScreen
import com.longhrk.mf.ui.viewmodel.AuthViewModel
import com.longhrk.mf.ui.viewmodel.MFChatViewModel

@Composable
fun NavGraph(
    eventHandler: EventHandler,
    navController: NavHostController
) {
    val startDestination = NavTarget.Splash.route

    val authViewModel = hiltViewModel<AuthViewModel>()
    val chatViewModel = hiltViewModel<MFChatViewModel>()

    NavHost(navController, startDestination) {
        composable(NavTarget.Splash.route) {
            SplashScreen(
                authViewModel = authViewModel,
                onLoginScreen = {
                    eventHandler.postNavEvent(
                        event = NavEvent.ActionWithPopUp(
                            target = NavTarget.Login,
                            popupTarget = NavTarget.Splash,
                            inclusive = true
                        )
                    )
                },
                onHomeScreen = {
                    eventHandler.postNavEvent(
                        event = NavEvent.ActionWithPopUp(
                            target = NavTarget.Home,
                            popupTarget = NavTarget.Splash,
                            inclusive = true
                        )
                    )
                },
            )
        }

        composable(NavTarget.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNextScreen = {
                    eventHandler.postNavEvent(
                        event = NavEvent.ActionWithPopUp(
                            target = NavTarget.Home,
                            popupTarget = NavTarget.Login,
                            inclusive = true
                        )
                    )
                }
            )
        }

        composable(NavTarget.Home.route) {
            HomeScreen(
                onNextScreen = {
                    eventHandler.postNavEvent(
                        event = NavEvent.Action(
                            target = NavTarget.Other
                        )
                    )
                },
                authViewModel = authViewModel,
                chatViewModel = chatViewModel
            )
        }

        composable(NavTarget.Other.route) {
            OtherScreen(
                onClick = { }
            )
        }
    }
}
