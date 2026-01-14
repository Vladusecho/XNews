package com.vladusecho.xnews.presentation

import com.vladusecho.xnews.domain.models.Article

sealed class MainState {

    object Initial : MainState()

    object Loading : MainState()

    data class Error(val error: String) : MainState()

    data class Content(val articles: List<Article>) : MainState()
}