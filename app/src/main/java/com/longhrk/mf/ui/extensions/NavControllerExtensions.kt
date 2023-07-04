package com.longhrk.mf.ui.extensions

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.longhrk.mf.ui.event.NavEvent
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun NavController.navigate(route: String, keyValue: Pair<String, String>) {
    val encodedParam = URLEncoder.encode(keyValue.second, StandardCharsets.UTF_8.toString())
    val newRoute = route.replace("{${keyValue.first}}", encodedParam)
    navigate(newRoute)
}

fun NavHostController.handleNavEvent(navEvent: NavEvent) {
    when (navEvent) {
        is NavEvent.None -> Unit
        is NavEvent.Action -> {
            navigate(navEvent.target.route)
        }
        is NavEvent.ActionWithValue -> {
            navigate(navEvent.target.route, navEvent.value)
        }
        is NavEvent.ActionInclusive -> {
            navigate(navEvent.target.route) {
                popUpTo(navEvent.inclusiveTarget.route) {
                    inclusive = true
                }
            }
        }
        is NavEvent.PopBackStackWithTarget -> {
            popBackStack(navEvent.target.route, inclusive = navEvent.inclusive)
        }
        is NavEvent.NavigateUp -> {
            navigateUp()
        }
        is NavEvent.PopBackStack -> {
            popBackStack()
        }
        is NavEvent.ActionWithPopUp -> {
            navigate(navEvent.target.route) {
                popUpTo(navEvent.popupTarget.route) {
                    inclusive = navEvent.inclusive
                }
            }
        }
    }
}