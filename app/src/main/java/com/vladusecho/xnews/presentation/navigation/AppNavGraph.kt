package com.vladusecho.xnews.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.vladusecho.xnews.presentation.screen.FavouriteScreen
import com.vladusecho.xnews.presentation.screen.MainScreen
import com.vladusecho.xnews.presentation.screen.MoreArticlesScreen
import com.vladusecho.xnews.presentation.screen.SearchScreen

@Composable
fun AppNavGraph(
    navigationState: NavigationState
) {

    NavHost(
        navController = navigationState.navHostController,
        startDestination = Screen.Main.route,
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 0))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 0))
        }
    ) {
        composable(Screen.Favorite.route) {
            FavouriteScreen()
        }
        //Nested nav graph
        navigation(
            startDestination = Screen.Home.route,
            route = Screen.Main.route,
        ) {
            composable(Screen.Home.route) {
                MainScreen(
                    onMoreClick = {
                        navigationState.navigateTo(Screen.MoreArticles.createRoute(it))
                    },
                    onSearchClick = {
                        navigationState.navigateTo(Screen.Search.route)
                    }
                )
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    onBackClick = {
                        navigationState.navigateTo(Screen.Home.route)
                    }
                )
            }
            composable(Screen.MoreArticles.route) {
                val topicTitle = Screen.MoreArticles.getTitle(it.arguments)
                MoreArticlesScreen(
                    topicTitle = topicTitle,
                    onBackClick = {
                        navigationState.navHostController.navigateUp()
                    }
                )
            }
        }
    }
}
