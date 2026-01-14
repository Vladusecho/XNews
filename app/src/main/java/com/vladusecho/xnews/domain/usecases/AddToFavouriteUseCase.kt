package com.vladusecho.xnews.domain.usecases

import com.vladusecho.xnews.domain.models.Article
import com.vladusecho.xnews.domain.repository.ArticlesRepository
import javax.inject.Inject

class AddToFavouriteUseCase @Inject constructor
    (private val repository: ArticlesRepository) {

    suspend operator fun invoke(article: Article) = repository.addToFavourite(article)
}