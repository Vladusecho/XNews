package com.vladusecho.xnews.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vladusecho.xnews.presentation.screen.FavouriteScreen
import com.vladusecho.xnews.presentation.screen.MainScreen
import com.vladusecho.xnews.presentation.screen.MoreArticlesScreen
import com.vladusecho.xnews.presentation.screen.SearchScreen

@Composable
fun AppNavGraph(
    navHostController: NavHostController
) {

    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route,
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 0))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 0))
        }
    ) {
        composable(Screen.Search.route) {
            SearchScreen(
                onBackClick = {
                    navHostController.navigateUp()
                }
            )
        }

        composable(Screen.Favorite.route) {
            FavouriteScreen()
        }
        composable(Screen.Home.route) {
            MainScreen(
                onMoreClick = {
                    navHostController.navigate(Screen.MoreArticles.createRoute(it)) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // only one same screen at the top
                        launchSingleTop = true
                        // save screen state after another screen
                        restoreState = true
                    }
                },
                onSearchClick = {
                    navHostController.navigate(Screen.Search.route) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // only one same screen at the top
                        launchSingleTop = true
                        // save screen state after another screen
                        restoreState = true
                    }
                }
            )
        }
        composable(Screen.MoreArticles.route) {
            val topicTitle = Screen.MoreArticles.getTitle(it.arguments)
            MoreArticlesScreen(
                topicTitle = topicTitle,
                onBackClick = {
                    navHostController.navigateUp()
                }
            )
        }
    }
}
