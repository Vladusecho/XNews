package com.vladusecho.xnews.data.repository

import android.util.Log
import com.vladusecho.xnews.data.mappers.toArticleDateFormat
import com.vladusecho.xnews.domain.models.Article
import com.vladusecho.xnews.domain.repository.ArticlesRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(

) : ArticlesRepository {


    val articles
        get() = mutableListOf<Article>().apply {
            repeat(20) {
                add(Article(
                    UUID.randomUUID().toString(),
                    "Антон Похиляк",
                    "$it Э ",
                    "Россия впервые с 2019 года появится на Венецианской биеннале современного искусства — с проектом «Дерево уходит корнями в небо». Об этом сообщает The Art Newspaper Russia",
                    "https://lenta.ru/news/2026/03/08/ukraina-vozmutilas-iz-za-vozvrascheniya-rossii-na-prestizhnuyu-mezhdunarodnuyu-vystavku/",
                    "https://icdn.lenta.ru/images/2026/03/08/18/20260308182707822/share_c71d67a04bbed4fc20fa304e0741f7f1.jpg",
                    "2026-03-08T15:27:07Z".toArticleDateFormat(),
                    source = "Lenta"
                ))
            }
        }

    override suspend fun loadSomeMainArticles(query: String, count: Int, page: Int): List<Article> {
        return articles.map { it.copy(title = it.title + page.toString() + query) }.take(count)
    }

    override suspend fun getFavouriteArticles(): List<Article> {
        TODO("Not yet implemented")
    }

    override fun getFavouriteArticlesFlow(): Flow<List<Article>> {
        TODO("Not yet implemented")
    }

    override suspend fun addToFavourite(article: Article) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFromFavourite(articleId: Int) {
        TODO("Not yet implemented")
    }
}