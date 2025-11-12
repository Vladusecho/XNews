package com.vladusecho.xnews.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainScreen() {

    val viewModel: MainViewModel = viewModel()

    viewModel.loadArticles("bitcoin")
}