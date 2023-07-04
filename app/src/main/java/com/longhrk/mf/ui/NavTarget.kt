package com.longhrk.mf.ui

sealed class NavTarget(val route: String) {

    object Splash : NavTarget("splash")
    object Login : NavTarget("login")
    object Home : NavTarget("home")
    object Other : NavTarget("other")

    override fun toString(): String {
        return route
    }
}