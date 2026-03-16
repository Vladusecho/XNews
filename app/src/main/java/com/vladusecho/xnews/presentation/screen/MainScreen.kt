package com.vladusecho.xnews.presentation.screen

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vladusecho.xnews.domain.models.Article
import com.vladusecho.xnews.presentation.customSnackbar.MySnackbarHost
import com.vladusecho.xnews.presentation.model.HeroFontFamily
import com.vladusecho.xnews.presentation.model.HotArticleCard
import com.vladusecho.xnews.presentation.model.MainArticleCard
import com.vladusecho.xnews.presentation.model.SecondaryArticleCard
import com.vladusecho.xnews.presentation.viewModel.MainCommand
import com.vladusecho.xnews.presentation.viewModel.MainState
import com.vladusecho.xnews.presentation.viewModel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    viewModel: MainViewModel = hiltViewModel()
) {
    val screenState = viewModel.state.collectAsState(MainState.Initial)
    val currentState = screenState.value

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val listState = rememberLazyListState()

    val lastId by viewModel.lastIndex.collectAsState()

    val lastArticleKey = remember(lastId) {
        Log.d(
            "LaunchedEffect",
            lastId
        )
        lastId
    }

    LaunchedEffect(listState, lastArticleKey) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.map { it.key }
        }.collect { visibleKeys ->
            if (lastId != "-1" && visibleKeys.contains(lastArticleKey)) {
                Log.d("LaunchedEffect", "last article is visible")
                viewModel.processCommand(MainCommand.LoadOthersNews)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        when (currentState) {
            is MainState.Content -> {
                LazyColumn(
                    state = listState
                ) {
                    currentState.content.forEach { content ->
                        if (content.isRow) {
                            item {
                                TopicLabel(
                                    topicName = content.title,
                                    onTopicClick = {}
                                )
                            }
                            item {
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    items(
                                        items = content.articles,
                                        key = { it.id + it.urlToImage }
                                    ) { article ->
                                        HotArticleCard(
                                            article = article,
                                            modifier = Modifier,
                                            onArticleClick = {
                                                showArticleInBrowser(article, context)
                                            }
                                        )
                                    }
                                }
                            }
                        } else if (content.isInfinityColumn) {
                            if (!content.isTitleInvisible) {
                                item {
                                    TopicLabel(
                                        topicName = content.title,
                                        isButtonVisible = false,
                                        onTopicClick = {}
                                    )
                                }
                            }
                            items(
                                items = content.articles,
                                key = { article -> article.id }
                            ) { article ->
                                Box(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    SecondaryArticleCard(
                                        article = article,
                                        onArticleClick = {
                                            showArticleInBrowser(article, context)
                                        }
                                    )
                                }
                            }
                        } else {
                            item {
                                TopicLabel(
                                    topicName = content.title,
                                    onTopicClick = {}
                                )
                            }
                            item {
                                MainArticleCard(
                                    article = content.articles.first(),
                                    onArticleClick = {
                                        showArticleInBrowser(content.articles.first(), context)
                                    }
                                )
                            }
                            content.articles.takeLast(3).forEachIndexed { index, article ->
                                item(
                                    key = article.id + article.urlToImage
                                ) {
                                    Box(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        SecondaryArticleCard(
                                            article = article,
                                            onArticleClick = {
                                                showArticleInBrowser(article, context)
                                            }
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
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(16.dp),
                                color = Color(0xffFF0606)
                            )
                        }
                    }
                }
            }

            is MainState.Error -> {

            }

            MainState.Initial -> {

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
fun TopicLabel(
    modifier: Modifier = Modifier,
    topicName: String,
    isButtonVisible: Boolean = true,
    onTopicClick: (String) -> Unit
) {

    Row(
        modifier = modifier
            .padding(16.dp)
            .clickable {
                onTopicClick(topicName)
            },
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
        if (isButtonVisible) {
            Text(
                text = "Смотреть больше...",
                fontFamily = HeroFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color(0xffFF0606),
                fontSize = 12.sp
            )
        }
    }
}

private fun showArticleInBrowser(
    article: Article,
    context: Context
) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = article.url.toUri()
    }
    context.startActivity(intent)
}