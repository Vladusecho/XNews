package com.vladusecho.xnews.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import coil3.compose.AsyncImage
import com.vladusecho.xnews.R
import com.vladusecho.xnews.domain.models.Article
import com.vladusecho.xnews.domain.navigation.AppNavGraph
import com.vladusecho.xnews.domain.navigation.MyNavigationItem
import com.vladusecho.xnews.domain.navigation.rememberNavState
import com.vladusecho.xnews.ui.theme.XNewsTheme

@Composable
fun MainScreen() {

    val navState = rememberNavState()

    val viewModel: MainViewModel = viewModel()

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .height(70.dp)
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
                    paddingValues,
                    viewModel
                )
            },
            favoriteScreenContent = { Text("favorite") },
            profileScreenContent = { Text("profile") }
        )
    }
}

@Composable
private fun HomeScreenContent(
    paddingValues: PaddingValues,
    viewModel: MainViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackbar by remember { mutableStateOf(false) }

    val screenState = viewModel.state.collectAsState(MainState.Initial)
    val currentState = screenState.value

    var text by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(paddingValues)
    ) {
        when (currentState) {
            is MainState.Content -> {
                LazyColumnWithSearchBar(
                    viewModel,
                    text,
                    { text = it }
                ) {
                    items(currentState.articles) {
                        Article(it)
                    }
                }
            }

            MainState.Initial -> {
                LazyColumnWithSearchBar(
                    viewModel,
                    text,
                    { text = it }
                )
            }

            MainState.Loading -> {
                LazyColumnWithSearchBar(
                    viewModel,
                    text,
                    { text = it }
                ) {
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

            is MainState.Error -> {
                LazyColumn {
                    item {
                        SearchBar(
                            viewModel, text
                        ) {
                            text = it
                        }
                    }
                    item {
                        showSnackbar = true
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            SnackbarHost(
                                hostState = snackbarHostState,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(11.dp)
                            ) {
                                Snackbar(
                                    it,
                                    contentColor = Color.Black, containerColor = Color.Cyan
                                )
                            }
                            LaunchedEffect(showSnackbar) {
                                if (showSnackbar) {
                                    snackbarHostState.showSnackbar(currentState.error)
                                    showSnackbar = false
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LazyColumnWithSearchBar(
    viewModel: MainViewModel,
    text: String,
    onValueChange: (String) -> Unit,
    newItem: LazyListScope.() -> Unit = {}
) {
    LazyColumn {
        item {
            SearchBar(
                viewModel, text
            ) {
                onValueChange(it)
            }
        }
        newItem()
    }
}

@Composable
private fun SearchBar(
    viewModel: MainViewModel,
    text: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { onValueChange(it) },
            label = { Text(text = "Введите запрос") }
        )
        Spacer(Modifier.width(7.dp))
        Icon(
            modifier = Modifier
                .size(56.5.dp)
                .clip(RoundedCornerShape(4.dp))
                .clickable {
                    viewModel.loadArticles(text)
                }
//                        .border(
//                            BorderStroke(1.08.dp, Color(0xff80828a)),
//                            shape = RoundedCornerShape(4.dp)
//                        )
                .background(Color(0xffeeedf6)),
            contentDescription = null,
            painter = painterResource(R.drawable.ic_search),
            tint = Color.Unspecified
        )
    }
}

@Composable
private fun Article(
    article: Article
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .shadow(
                elevation = 8.dp,
                shape = MaterialTheme.shapes.medium,
                clip = true
            )
            .clip(CircleShape.copy(CornerSize(13.dp)))
            .background(Color.White)
    ) {
        Column {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize(),
                model = article.urlToImage ?: "",
                contentDescription = null,
                contentScale = ContentScale.FillWidth
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

@Preview
@Composable
private fun MainScreenPreview() {
    XNewsTheme {
        MainScreen()
    }
}

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