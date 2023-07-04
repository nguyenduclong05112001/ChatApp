package com.longhrk.mf

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.longhrk.mf.ui.EventHandler
import com.longhrk.mf.ui.NavGraph
import com.longhrk.mf.ui.extensions.handleNavEvent
import com.longhrk.mf.ui.theme.MF_Theme
import com.longhrk.mf.ui.viewmodel.NavViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val navigationViewModel by viewModels<NavViewModel>()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val eventHandler = remember {
                EventHandler(navigationViewModel)
            }
            MF_Theme(darkTheme = true) {
                GraphMainApp(eventHandler)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
private fun GraphMainApp(eventHandler: EventHandler) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        eventHandler.navEvent().collect {
            navController.handleNavEvent(it)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        NavGraph(eventHandler = eventHandler, navController = navController)
    }
}