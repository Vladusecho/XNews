package com.vladusecho.xnews.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vladusecho.xnews.presentation.model.Topic
import com.vladusecho.xnews.presentation.screen.FavouriteScreen
import com.vladusecho.xnews.presentation.screen.HomeScreenContent
import com.vladusecho.xnews.presentation.screen.MoreArticlesScreen

@Composable
fun AppNavGraph(
    navHostController: NavHostController
) {

    NavHost(
        navHostController,
        startDestination = Screen.Home.route,
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
        composable(Screen.Home.route) {
            HomeScreenContent(
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