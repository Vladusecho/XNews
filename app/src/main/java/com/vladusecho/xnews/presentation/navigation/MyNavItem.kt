package com.vladusecho.xnews.presentation.navigation

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