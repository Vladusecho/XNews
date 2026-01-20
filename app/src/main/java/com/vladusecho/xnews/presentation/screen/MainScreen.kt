package com.vladusecho.xnews.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vladusecho.xnews.domain.navigation.AppNavGraph
import com.vladusecho.xnews.domain.navigation.MyNavigationItem
import com.vladusecho.xnews.domain.navigation.Screen
import com.vladusecho.xnews.domain.navigation.rememberNavState
import com.vladusecho.xnews.presentation.state.MainState
import com.vladusecho.xnews.presentation.viewModel.MainViewModel
import com.vladusecho.xnews.presentation.customSnackbar.MySnackbarHost
import com.vladusecho.xnews.presentation.mySwiper.MySwiper
import com.vladusecho.xnews.presentation.mySwiper.SwipeActionType
import com.vladusecho.xnews.presentation.viewModel.ViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModelFactory: ViewModelFactory) {

    val viewModel: MainViewModel = viewModel(factory = viewModelFactory)

    val navState = rememberNavState()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background
            ) {
                val navBackStackEntry by navState.navHostController.currentBackStackEntryAsState()
                val items = listOf(
                    MyNavigationItem.Home,
                    MyNavigationItem.Favorite,
                    MyNavigationItem.Profile
                )

                val unseenNewsState = viewModel.unseenNews.collectAsState()
                val unseenNews = unseenNewsState.value
                val isWatchedFavouriteState = viewModel.isWatchedFavourite.collectAsState()
                val isWatchedFavourite = isWatchedFavouriteState.value

                items.forEach { item ->
                    val selected = navBackStackEntry?.destination?.hierarchy?.any {
                        it.route == item.screen.route
                    } ?: false

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                navState.navigateTo(item.screen.route)
                            }
                            if (item.screen.route == Screen.ROUTE_FAVORITE) {
                                viewModel.invisibleCounter()
                            }
                            if (item.screen.route == Screen.ROUTE_HOME) {
                                viewModel.visibleCounter()
                            }
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (!isWatchedFavourite) {
                                        if (unseenNews > 0 && item.screen.route == Screen.ROUTE_FAVORITE) {
                                            Badge {
                                                Text("$unseenNews")
                                            }
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    null
                                )
                            }
                        },
                        label = {
                            Text(item.title)
                        },
                    )
                }
            }
        }
    ) { paddingValues ->
        AppNavGraph(
            navState.navHostController,
            homeScreenContent = {
                HomeScreenContent(
                    paddingValues, viewModel
                )
            },
            favoriteScreenContent = {
                FavouriteScreen(
                    paddingValues, viewModelFactory
                )
            },
            profileScreenContent = { Text("profile") }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    paddingValues: PaddingValues,
    viewModel: MainViewModel
) {
    val screenState = viewModel.state.collectAsState(MainState.Initial)
    val currentState = screenState.value

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.onSecondary)
            .padding(paddingValues)
            .border(BorderStroke(0.5.dp, MaterialTheme.colorScheme.onSecondary))
    ) {
        LazyColumn {
            item {
                AppNameWithSearchBar(viewModel)
            }
            when (currentState) {
                is MainState.Content -> {
                    items(items = currentState.articles) {
                        Box(
                            modifier = Modifier.animateItem()
                        ) {
                            MySwiper(
                                article = it,
                                swipeActionType = SwipeActionType.ADD_TO_FAVOURITE
                            ) {
                                viewModel.addToFavourite(it)
                                viewModel.incrementNewFavouriteCount()
                                scope.launch {
                                    snackbarHostState.showSnackbar("✅ Успешно добавлено!")
                                }
                            }
                        }
                    }
                }

                is MainState.Error -> {
                    scope.launch {
                        snackbarHostState.showSnackbar("❌ Что-то пошло не так...")
                    }
                }

                MainState.Initial -> {
                    // Сделать начальные главные новости
                }

                MainState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
        MySnackbarHost(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.BottomCenter),
            snackbarHostState
        )
    }
}

@Composable
fun AppNameWithSearchBar(
    viewModel: MainViewModel
) {

    val searchQueryState = viewModel.searchQuery.collectAsState()
    val searchQuery = searchQueryState.value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "XNews",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            "Developed by Vladusecho <3",
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = searchQuery,
            onValueChange = { query ->
                viewModel.onSearchQueryChanged(query)
            },
            label = { Text(text = "Введите запрос") },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.background,
            ),
            singleLine = true
        )
    }
}