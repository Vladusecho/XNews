package com.vladusecho.xnews.presentation.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vladusecho.xnews.presentation.model.HeroFontFamily
import com.vladusecho.xnews.presentation.model.SecondaryArticleCard
import com.vladusecho.xnews.presentation.model.Topic
import com.vladusecho.xnews.presentation.viewModel.MainCommand
import com.vladusecho.xnews.presentation.viewModel.MainViewModel
import com.vladusecho.xnews.presentation.viewModel.MoreArticlesCommand
import com.vladusecho.xnews.presentation.viewModel.MoreArticlesState
import com.vladusecho.xnews.presentation.viewModel.MoreArticlesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreArticlesScreen(
    modifier: Modifier = Modifier,
    topicTitle: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    val viewModel: MoreArticlesViewModel = hiltViewModel(
        creationCallback = { factory: MoreArticlesViewModel.Factory ->
            factory.create(topicTitle)
        }
    )

    val state = viewModel.state.collectAsState()
    val currentState = state.value

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
                viewModel.processCommand(MoreArticlesCommand.LoadOthersNews)
            }
        }
    }

    Scaffold(
        topBar = {
            Column() {
                TopAppBar(
                    title = {
                        Text(
                            text = topicTitle,
                            fontFamily = HeroFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                            fontSize = 24.sp,
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton({
                            onBackClick()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "back",
                                tint = Color.Black
                            )
                        }
                    }
                )
                HorizontalDivider(
                    modifier = Modifier,
                    thickness = 0.5.dp,
                    color = Color.Gray.copy(alpha = 0.5f)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .background(color = Color.White)
        ) {
            when(currentState) {
                is MoreArticlesState.Content -> {
                    LazyColumn(
                        state = listState
                    ) {
                        items(
                            items = currentState.articlesList,
                            key = { it.id }
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
                MoreArticlesState.Initial -> {}
                MoreArticlesState.Loading -> {
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
        }
    }
}