package com.vladusecho.xnews.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vladusecho.xnews.presentation.screen.FavouriteScreen
import com.vladusecho.xnews.presentation.screen.HomeScreenContent

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
            HomeScreenContent()
        }
    }

}