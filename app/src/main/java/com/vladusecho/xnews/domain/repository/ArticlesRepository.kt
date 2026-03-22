package com.vladusecho.xnews.domain.repository

import com.vladusecho.xnews.domain.models.Article
import kotlinx.coroutines.flow.Flow

interface ArticlesRepository {

    suspend fun loadSomeMainArticles(query: String, count: Int, page: Int): List<Article>

    suspend fun getFavouriteArticles(): List<Article>
    fun getFavouriteArticlesFlow(): Flow<List<Article>>

    suspend fun addToFavourite(article: Article)

    suspend fun deleteFromFavourite(articleId: Int)
}