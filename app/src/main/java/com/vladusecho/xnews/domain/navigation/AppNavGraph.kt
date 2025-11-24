package com.vladusecho.xnews.domain.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    homeScreenContent: @Composable () -> Unit,
    favoriteScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable () -> Unit
) {

    NavHost(
        navHostController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Favorite.route) {
            favoriteScreenContent()
        }
        composable(Screen.Home.route) {
            homeScreenContent()
        }
        composable(Screen.Profile.route) {
            profileScreenContent()
        }
    }

}