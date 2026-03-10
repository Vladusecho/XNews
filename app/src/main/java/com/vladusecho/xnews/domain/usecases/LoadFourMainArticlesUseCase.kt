package com.vladusecho.xnews.domain.usecases

import com.vladusecho.xnews.domain.models.Article
import com.vladusecho.xnews.domain.repository.ArticlesRepository
import javax.inject.Inject

class LoadFourMainArticlesUseCase @Inject constructor(
    private val repository: ArticlesRepository
) {

    suspend operator fun invoke(query: String): List<Article> {
        return repository.loadFourMainArticles(query)
    }
}