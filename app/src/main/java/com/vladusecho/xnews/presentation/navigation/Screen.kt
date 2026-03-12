package com.vladusecho.xnews.presentation.navigation

sealed class Screen(
    val route: String
) {

    object Home: Screen(ROUTE_HOME)

    object Favorite: Screen(ROUTE_FAVORITE)
    object Settings: Screen(ROUTE_SETTINGS)
    object Profile: Screen(ROUTE_PROFILE)


    companion object {
        const val ROUTE_HOME = "home"
        const val ROUTE_FAVORITE = "favorite"
        const val ROUTE_SETTINGS = "settings"
        const val ROUTE_PROFILE = "profile"
    }
}