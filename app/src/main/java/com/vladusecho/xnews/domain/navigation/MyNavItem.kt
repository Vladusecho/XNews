package com.vladusecho.xnews.domain.navigation

import android.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MyNavigationItem(
    val screen: Screen,
    val icon: ImageVector,
    val title: String
) {

    object Home : MyNavigationItem(
        screen = Screen.Home,
        icon = Icons.Outlined.Home,
        title = "Главная"
    )

    object Favorite : MyNavigationItem(
        screen = Screen.Favorite,
        icon = Icons.Outlined.Favorite,
        title = "Избранное"
    )
}