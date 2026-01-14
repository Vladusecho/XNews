package com.vladusecho.xnews.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.vladusecho.xnews.presentation.MainViewModel

@Composable
fun FavouriteScreen(
    paddingValues: PaddingValues,
    viewModel: MainViewModel
) {

    val favouriteArticlesState = viewModel.getFavouriteArticles().collectAsState(listOf())
    val favouriteList = favouriteArticlesState.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSecondary)
            .padding(paddingValues)
    ) {
        LazyColumn {
            items(items = favouriteList){
                Article(
                    it
                ) {
                    viewModel.deleteFromFavourite(it.id)
                }
            }
        }
    }
}