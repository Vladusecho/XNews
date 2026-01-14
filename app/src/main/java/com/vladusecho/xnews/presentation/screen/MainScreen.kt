package com.vladusecho.xnews.presentation.screen

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import coil3.compose.AsyncImage
import com.vladusecho.xnews.R
import com.vladusecho.xnews.domain.models.Article
import com.vladusecho.xnews.domain.navigation.AppNavGraph
import com.vladusecho.xnews.domain.navigation.MyNavigationItem
import com.vladusecho.xnews.domain.navigation.Screen
import com.vladusecho.xnews.domain.navigation.rememberNavState
import com.vladusecho.xnews.presentation.ImgState
import com.vladusecho.xnews.presentation.MainState
import com.vladusecho.xnews.presentation.MainViewModel
import com.vladusecho.xnews.ui.theme.XNewsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {

    val navState = rememberNavState()

    Scaffold(
        topBar = {
            val currentRoute =
                navState.navHostController.currentBackStackEntryAsState().value?.destination?.route
            val title = when (currentRoute) {
                Screen.ROUTE_HOME -> "XNews"
                Screen.ROUTE_PROFILE -> "Профиль"
                Screen.ROUTE_FAVORITE -> "Избранное"
                else -> "Error"
            }
            TopAppBar(
                title = {
                    Text(title)
                },
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(containerColor = MaterialTheme.colorScheme.background)
            )
        },
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
                        },
                        icon = {
                            Icon(item.icon, null)
                        },
                        label = {
                            Text(item.title)
                        }
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
                    paddingValues, viewModel
                )
            },
            profileScreenContent = { Text("profile") }
        )
    }
}

@Composable
private fun HomeScreenContent(
    paddingValues: PaddingValues,
    viewModel: MainViewModel
) {
    LazyColumnWithSearchBar(paddingValues, viewModel)
}

@Composable
private fun ShowSnackbar(
    textError: String
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackbar by remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(7.dp)
        ) {
            Snackbar(
                it,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        }
        LaunchedEffect(showSnackbar) {
            if (showSnackbar) {
                snackbarHostState.showSnackbar(textError)
                showSnackbar = false
            }
        }
    }
}

@Composable
private fun LazyColumnWithSearchBar(
    paddingValues: PaddingValues,
    viewModel: MainViewModel
) {
    val screenState = viewModel.state.collectAsState(MainState.Initial)
    val currentState = screenState.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.onSecondary)
            .padding(paddingValues)
            .border(BorderStroke(0.5.dp, MaterialTheme.colorScheme.onSecondary))
    ) {
        LazyColumn {
            item {
                MySearchBar(viewModel)
            }
            when (currentState) {
                is MainState.Content -> {
                    items(items = currentState.articles) {
                        Article(it) {
                            viewModel.addToFavourite(it)
                        }
                    }
                }

                is MainState.Error ->
                    item {
                        ShowSnackbar(currentState.error)
                    }

                MainState.Initial -> {

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
    }
}

@Composable
private fun MySearchBar(
    viewModel: MainViewModel
) {

    val searchQuery = viewModel.searchQuery.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        TextField(
            value = searchQuery.value,
            onValueChange = { query ->
                viewModel.onSearchQueryChanged(query)
            },
            label = { Text(text = "Введите запрос") },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.background
            ),
            singleLine = true
        )
        Spacer(Modifier.width(7.dp))
        Icon(
            modifier = Modifier
                .size(56.5.dp)
                .clip(RoundedCornerShape(4.dp))
                .clickable {
                    viewModel.performSearch(searchQuery.value)
                }
                .background(MaterialTheme.colorScheme.background),
            contentDescription = null,
            painter = painterResource(R.drawable.ic_search),
            tint = Color.Unspecified
        )
    }
}

@Composable
fun Article(
    article: Article,
    onLongClick: () -> Unit
) {

    var isLoading : ImgState by remember { mutableStateOf(ImgState.Initial) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .padding(16.dp)
            .shadow(
                elevation = 8.dp,
                shape = MaterialTheme.shapes.medium,
                clip = true
            )
            .clip(CircleShape.copy(CornerSize(13.dp)))
            .background(MaterialTheme.colorScheme.background)
            .combinedClickable(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, article.url.toUri())
                    context.startActivity(intent)
                },
                onLongClick = {
                    onLongClick()
                }
            )
    ) {
        Column {

            when(isLoading){
                ImgState.Initial -> {}
                ImgState.LoadedImg -> {}
                ImgState.LoadingImg -> {
                    Box(
                        modifier = Modifier.fillMaxSize().height(140.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                ImgState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize().height(140.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = ""
                        )
                    }
                }
            }

            AsyncImage(
                modifier = Modifier
                    .fillMaxSize(),
                model = article.urlToImage ?: "",
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                onLoading = {
                    isLoading = ImgState.LoadingImg
                },
                onSuccess = {
                    isLoading = ImgState.LoadedImg
                },
                onError = {
                    isLoading = ImgState.Error
                }
            )

//            Image(
//                painter = painterResource(R.drawable.img_post_example),
//                contentDescription = null
//            )
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
            ) {
                Text(
                    text = article.getDateWithAuthor(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraLight
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = article.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = article.description,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

//@Preview
//@Composable
//private fun MainScreenPreview() {
//    XNewsTheme(darkTheme = false) {
//        MainScreen()
//    }
//}

//@Preview
//@Composable
//private fun ArticlePreview() {
//    Box(
//        modifier = Modifier.fillMaxSize()
//            .background(Color.White),
//    ) {
//
//    }
//    Article(ArticleDto(
//        1,
//        "vlad",
//        "Inside a Wild Bitcoin Heist: Five-Star Hotels, Cash-Stuffed Envelopes, and Vanishing Funds",
//        "Sophisticated crypto scams are on the rise. But few of them go to the lengths one bitcoin mining executive experienced earlier this year.",
//        "https://www.wired.com/story/bitcoin-scam-mining-as-service/",
//        "https://gizmodo.com/app/uploads/2024/04/0ddbd47a359dbefbb14c16d0ffe99a95.jpg",
//        "2025-11-17T10:00:00Z",
//        ""
//    ))
//}