package com.vladusecho.xnews.data.repository

import com.vladusecho.xnews.data.mappers.mapperArticleDtoToArticle
import com.vladusecho.xnews.data.remote.ApiFactory
import com.vladusecho.xnews.domain.models.Article
import com.vladusecho.xnews.domain.repository.ArticlesRepository
import java.util.Date

object ArticlesRepositoryImpl : ArticlesRepository {

    override suspend fun loadArticles(query: String): List<Article> {
        return ApiFactory.apiService.getArticles(query).articles.map {
            mapperArticleDtoToArticle(it)
        }.sortedByDescending { it.date }
    }
}