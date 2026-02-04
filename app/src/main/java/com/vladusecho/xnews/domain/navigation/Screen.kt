package com.vladusecho.xnews.domain.navigation

sealed class Screen(
    val route: String
) {

    object Home: Screen(ROUTE_HOME)

    object Favorite: Screen(ROUTE_FAVORITE)


    companion object {
        const val ROUTE_HOME = "home"
        const val ROUTE_FAVORITE = "favorite"
    }
}