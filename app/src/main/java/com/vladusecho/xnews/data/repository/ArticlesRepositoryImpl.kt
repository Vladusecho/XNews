package com.vladusecho.xnews.data.repository

import com.vladusecho.xnews.data.local.Dao
import com.vladusecho.xnews.data.mappers.mapperArticleDtoToArticle
import com.vladusecho.xnews.data.mappers.mapperArticleModelToArticle
import com.vladusecho.xnews.data.mappers.mapperArticleModelsToArticle
import com.vladusecho.xnews.data.mappers.mapperArticleToArticleModel
import com.vladusecho.xnews.data.remote.ApiService
import com.vladusecho.xnews.domain.models.Article
import com.vladusecho.xnews.domain.repository.ArticlesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ArticlesRepositoryImpl @Inject constructor(
    private val dao: Dao,
    private val apiService: ApiService
) : ArticlesRepository {

    override suspend fun loadSomeMainArticles(query: String, count: Int, page: Int): List<Article> {
        return apiService.getSomeMainArticles(query, count, page).articles.map {
            mapperArticleDtoToArticle(it)
        }
    }

    override suspend fun getFavouriteArticles(): List<Article> =
        dao.getFavouriteArticles().mapperArticleModelsToArticle()

    override fun getFavouriteArticlesFlow(): Flow<List<Article>> {
        return dao.getFavouriteArticlesFlow().map { it.mapperArticleModelsToArticle() }
    }

    override suspend fun addToFavourite(article: Article) {
        dao.addToFavourite(article.mapperArticleToArticleModel())
    }

    override suspend fun deleteFromFavourite(articleId: Int) {
        dao.deleteFromFavourite(articleId)
    }

}