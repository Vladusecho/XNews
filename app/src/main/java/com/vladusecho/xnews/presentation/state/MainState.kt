package com.vladusecho.xnews.presentation.state

import com.vladusecho.xnews.domain.models.Article

sealed class MainState {

    object Initial : MainState()

    data class MainNews(val initArticles: List<Article>) : MainState()

    object Loading : MainState()

    data class Error(val error: String) : MainState()

    data class Content(val articles: List<Article>) : MainState()
}