package com.vladusecho.xnews.presentation.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vladusecho.xnews.presentation.customSnackbar.MySnackbarHost
import com.vladusecho.xnews.presentation.mySwiper.MySwiper
import com.vladusecho.xnews.presentation.mySwiper.SwipeActionType
import com.vladusecho.xnews.presentation.state.FavouriteState
import com.vladusecho.xnews.presentation.viewModel.FavouriteViewModel
import com.vladusecho.xnews.presentation.viewModel.ViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun FavouriteScreen(
    paddingValues: PaddingValues,
    viewModelFactory: ViewModelFactory
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val viewModel: FavouriteViewModel = viewModel(factory = viewModelFactory)

    val screenState = viewModel.state.collectAsState(FavouriteState.Initial)
    val currentState = screenState.value

    val favArticlesState = viewModel.favouriteArticles.collectAsState()
    val favArticlesFlow = favArticlesState.value.collectAsState(listOf())
    val articles = favArticlesFlow.value

    viewModel.getFavouriteArticles().also { Log.d("FavouriteScreen", "work") }

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

                    items(
                        items = articles,
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

