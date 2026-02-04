package com.vladusecho.xnews.domain.usecases

import com.vladusecho.xnews.domain.repository.ArticlesRepository
import javax.inject.Inject

class LoadFavouriteArticlesUseCase @Inject constructor
    (private val repository: ArticlesRepository) {

    suspend operator fun invoke() = repository.getFavouriteArticles()
}