package com.vladusecho.xnews.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vladusecho.xnews.presentation.customSnackbar.MySnackbarHost
import com.vladusecho.xnews.presentation.model.HeroFontFamily
import com.vladusecho.xnews.presentation.model.HotArticleCard
import com.vladusecho.xnews.presentation.model.MainArticleCard
import com.vladusecho.xnews.presentation.model.SecondaryArticleCard
import com.vladusecho.xnews.presentation.state.ImgState
import com.vladusecho.xnews.presentation.viewModel.MainState
import com.vladusecho.xnews.presentation.viewModel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    viewModel: MainViewModel = hiltViewModel()
) {
    val screenState = viewModel.state.collectAsState(MainState.Initial)
    val currentState = screenState.value

//    val isDuplicateState = viewModel.isDuplicate.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val listState = rememberLazyListState()

//    val showFab by remember {
//        derivedStateOf {
//            listState.firstVisibleItemIndex > 0
//        }
//    }

    val politicArticlesState = viewModel.politicArticlesFlow.collectAsState()
    val politicArticles = politicArticlesState.value
    val businessArticlesState = viewModel.businessArticlesFlow.collectAsState()
    val businessArticles = businessArticlesState.value
    val economicArticlesState = viewModel.economicArticlesFlow.collectAsState()
    val economicArticles = economicArticlesState.value
    val hotArticlesState = viewModel.hotArticlesFlow.collectAsState()
    val hotArticles = hotArticlesState.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        when (currentState) {
            MainState.Initial -> {
                LazyColumn {
                    item {
                        TopicLabel(
                            topicName = "Горячие новости",
                            onTopicClick = {}
                        )
                    }
                    if (hotArticles.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = Color(0xffFF0606)
                                )
                            }
                        }
                    } else {
                        item {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                items(
                                    items = hotArticles,
                                    key = { it.id.toString() + it.urlToImage }
                                ) {
                                    HotArticleCard(
                                        article = it,
                                        modifier = Modifier
                                    )
                                }
                            }
                        }
                    }
                    item {
                        TopicLabel(
                            topicName = "Политика",
                            onTopicClick = {}
                        )
                    }
                    if (politicArticles.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = Color(0xffFF0606)
                                )
                            }
                        }
                    } else {

                        item {
                            MainArticleCard(
                                article = politicArticles.first()
                            )
                        }
                        politicArticles.takeLast(3).forEachIndexed { index, article ->
                            item(
                                key = article.id.toString() + article.urlToImage
                            ) {
                                Box(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    SecondaryArticleCard(
                                        article = article
                                    )
                                }
                                if (index != 2) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        color = Color.Gray.copy(alpha = 0.2f),
                                        thickness = 1.dp
                                    )
                                }
                            }
                        }
                    }
                    item {
                        TopicLabel(
                            topicName = "Бизнес",
                            onTopicClick = {}
                        )
                    }
                    if (businessArticles.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = Color(0xffFF0606)
                                )
                            }
                        }
                    } else {

                        item {
                            MainArticleCard(
                                article = businessArticles.first()
                            )
                        }
                        businessArticles.takeLast(3).forEachIndexed { index, article ->
                            item(
                                key = article.id + article.urlToImage
                            ) {
                                Box(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    SecondaryArticleCard(
                                        article = article
                                    )
                                }
                                if (index != 2) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        color = Color.Gray.copy(alpha = 0.2f),
                                        thickness = 1.dp
                                    )
                                }
                            }
                        }
                    }
                    item {
                        TopicLabel(
                            topicName = "Экономика",
                            onTopicClick = {}
                        )
                    }
                    if (economicArticles.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = Color(0xffFF0606)
                                )
                            }
                        }
                    } else {

                        item {
                            MainArticleCard(
                                article = economicArticles.first()
                            )
                        }
                        economicArticles.takeLast(3).forEachIndexed { index, article ->
                            item(
                                key = article.id.toString() + article.urlToImage
                            ) {
                                Box(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    SecondaryArticleCard(
                                        article = article
                                    )
                                }
                                if (index != 2) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        color = Color.Gray.copy(alpha = 0.2f),
                                        thickness = 1.dp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is MainState.Error -> {
                LaunchedEffect(Unit) {
                    scope.launch {
                        snackbarHostState.showSnackbar("❌ Что-то пошло не так...")
                    }
                }
            }

            MainState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xffFF0606)
                    )
                }
            }

            is MainState.Content -> TODO()
        }

        MySnackbarHost(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.BottomCenter),
            snackbarHostState
        )
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(10.dp),
//            contentAlignment = Alignment.BottomEnd
//        ) {
//            AnimatedFAB(
//                showFab, listState
//            )
//        }
    }
}

@Composable
fun AnimatedFAB(
    showFab: Boolean,
    listState: LazyListState
) {
    val scope = rememberCoroutineScope()

    AnimatedVisibility(
        visible = showFab,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut()
    ) {
        FloatingActionButton(
            onClick = {
                scope.launch {
                    listState.animateScrollToItem(0)
                }
            },
            containerColor = MaterialTheme.colorScheme.onSecondary,
            contentColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .padding(bottom = 16.dp)
        ) {
            Icon(Icons.Default.ArrowUpward, null)
        }
    }
}

@Composable
fun TopicLabel(
    modifier: Modifier = Modifier,
    topicName: String,
    onTopicClick: (String) -> Unit
) {

    Row(
        modifier = modifier
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        VerticalDivider(
            modifier = Modifier
                .height(32.dp)
                .padding(horizontal = 8.dp),
            color = Color(0xffFF0606),
            thickness = 3.dp
        )
        Text(
            text = topicName,
            fontFamily = HeroFontFamily,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            fontSize = 18.sp
        )
        Spacer(modifier.weight(1f))
        Text(
            text = "Смотреть больше...",
            fontFamily = HeroFontFamily,
            fontWeight = FontWeight.Normal,
            color = Color(0xffFF0606),
            fontSize = 12.sp
        )
    }
}