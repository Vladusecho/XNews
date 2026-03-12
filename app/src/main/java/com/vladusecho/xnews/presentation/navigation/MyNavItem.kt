package com.vladusecho.xnews.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource

sealed class MyNavigationItem(
    val screen: Screen,
    val title: String
) {

    object Home : MyNavigationItem(
        screen = Screen.Home,
        title = "Главная"
    )

    object Favorite : MyNavigationItem(
        screen = Screen.Favorite,
        title = "Избранное"
    )

    object Profile : MyNavigationItem(
        screen = Screen.Profile,
        title = "Профиль"
    )
    object Settings : MyNavigationItem(
        screen = Screen.Settings,
        title = "Настройки"
    )
}