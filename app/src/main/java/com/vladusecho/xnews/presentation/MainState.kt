package com.vladusecho.xnews.presentation

import com.vladusecho.xnews.data.models.ArticleDto

sealed class MainState {

    object Initial : MainState()

    object Loading : MainState()

    data class Content(val articles: List<ArticleDto>) : MainState()
}