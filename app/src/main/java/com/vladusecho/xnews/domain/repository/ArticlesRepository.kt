package com.vladusecho.xnews.domain.repository

import com.vladusecho.xnews.domain.models.Article

interface ArticlesRepository {

    suspend fun loadArticles(query: String): List<Article>
}