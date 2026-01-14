package com.vladusecho.xnews.domain.repository

import com.vladusecho.xnews.domain.models.Article
import kotlinx.coroutines.flow.Flow

interface ArticlesRepository {

    suspend fun loadArticles(query: String): List<Article>

    val favouriteArticles: Flow<List<Article>>

    suspend fun addToFavourite(article: Article)

    suspend fun deleteFromFavourite(articleId: Int)
}