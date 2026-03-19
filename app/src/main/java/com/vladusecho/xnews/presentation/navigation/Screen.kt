package com.vladusecho.xnews.presentation.navigation

import android.os.Bundle

sealed class Screen(
    val route: String
) {

    object Home: Screen(ROUTE_HOME)

    object Favorite: Screen(ROUTE_FAVORITE)
    object Settings: Screen(ROUTE_SETTINGS)
    object Profile: Screen(ROUTE_PROFILE)
    object MoreArticles: Screen(ROUTE_MORE_ARTICLES) {

        fun createRoute(title: String): String {
            return "more_articles/$title"
        }

        fun getTitle(args: Bundle?): String {
            return args?.getString("title") ?: ""
        }
    }


    companion object {
        const val ROUTE_HOME = "home"
        const val ROUTE_FAVORITE = "favorite"
        const val ROUTE_SETTINGS = "settings"
        const val ROUTE_PROFILE = "profile"
        const val ROUTE_MORE_ARTICLES = "more_articles/{title}"
    }
}