package com.vladusecho.xnews.presentation.screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vladusecho.xnews.presentation.customSnackbar.MySnackbarHost
import com.vladusecho.xnews.presentation.mySwiper.MySwiper
import com.vladusecho.xnews.presentation.mySwiper.SwipeActionType
import com.vladusecho.xnews.presentation.state.FavouriteState
import com.vladusecho.xnews.presentation.viewModel.FavouriteViewModel
import com.vladusecho.xnews.presentation.viewModel.ViewModelFactory
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreen(
    viewModelFactory: ViewModelFactory
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val viewModel: FavouriteViewModel = viewModel(factory = viewModelFactory)

    val screenState = viewModel.state.collectAsState(FavouriteState.Initial)
    val currentState = screenState.value

    val articles = viewModel.articles.collectAsState(emptyList())

    var showDialog by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Избранное (${articles.value.size})") },
                actions = {
                    IconButton({
                        showDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.QuestionMark,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {  }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onSecondary)
                .padding(paddingValues)
                .border(BorderStroke(0.5.dp, MaterialTheme.colorScheme.onSecondary))
        ) {
            LazyColumn {
                when (currentState) {
                    FavouriteState.Initial -> {}
                    FavouriteState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Загрузка...")
                            }
                        }
                    }

                    is FavouriteState.Content -> {

                        if (articles.value.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text("Смахните любую статью влево, чтобы добавить ее в избранное!")
                                }
                            }
                        }

                        items(
                            items = articles.value,
                            key = { it.id })
                        {
                            Box(
                                modifier = Modifier.animateItem()
                            ) {
                                MySwiper(
                                    article = it,
                                    swipeActionType = SwipeActionType.DELETE_FROM_FAVOURITE
                                ) {
                                    viewModel.deleteFromFavourite(it.id)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("\uD83D\uDDD1\uFE0F Успешно удалено!")
                                    }
                                }
                            }
                        }
                    }
                }
            }
            MySnackbarHost(snackbarHostState = snackbarHostState,
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.BottomCenter))
        }
    }
    if (showDialog) {
        BasicAlertDialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties()
        ) {
            Card(
            ) {
                Column(
                    modifier = Modifier.padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Смахните влево, чтобы удалить статью из избранного!")
                    TextButton(
                        onClick = { showDialog = false }
                    ) {
                        Text("Спасибо!")
                    }
                }
            }
        }
    }
}

