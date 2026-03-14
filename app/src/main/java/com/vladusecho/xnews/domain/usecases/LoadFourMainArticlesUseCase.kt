package com.vladusecho.xnews.domain.usecases

import com.vladusecho.xnews.domain.models.Article
import com.vladusecho.xnews.domain.repository.ArticlesRepository
import javax.inject.Inject

class LoadSomeMainArticlesUseCase @Inject constructor(
    private val repository: ArticlesRepository
) {

    suspend operator fun invoke(query: String, count: Int, page: Int = 1): List<Article> {
        return repository.loadSomeMainArticles(query, count, page)
    }
}