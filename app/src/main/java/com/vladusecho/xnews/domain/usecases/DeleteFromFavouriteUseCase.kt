package com.vladusecho.xnews.domain.usecases

import com.vladusecho.xnews.domain.repository.ArticlesRepository
import javax.inject.Inject

class DeleteFromFavouriteUseCase @Inject constructor
    (private val repository: ArticlesRepository) {

    suspend operator fun invoke(articleId: Int) = repository.deleteFromFavourite(articleId)
}