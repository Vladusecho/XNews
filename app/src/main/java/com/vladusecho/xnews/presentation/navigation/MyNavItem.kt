package com.vladusecho.xnews.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.vladusecho.xnews.R

sealed class MyNavigationItem(
    val screen: Screen,
    val title: String,
    val iconId: Int
) {

    object Home : MyNavigationItem(
        screen = Screen.Main,
        title = "Главная",
        iconId = R.drawable.ic_home
    )

    object Favorite : MyNavigationItem(
        screen = Screen.Favorite,
        title = "Избранное",
        iconId = R.drawable.ic_bookmark
    )

    object Profile : MyNavigationItem(
        screen = Screen.Profile,
        title = "Профиль",
        iconId = R.drawable.ic_user
    )

    object Settings : MyNavigationItem(
        screen = Screen.Settings,
        title = "Настройки",
        iconId = R.drawable.ic_settings
    )
}