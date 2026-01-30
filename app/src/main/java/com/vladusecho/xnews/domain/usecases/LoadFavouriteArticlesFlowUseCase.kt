package com.vladusecho.xnews.domain.usecases

import com.vladusecho.xnews.domain.repository.ArticlesRepository
import javax.inject.Inject

class LoadFavouriteArticlesFlowUseCase @Inject constructor(
    private val repository: ArticlesRepository
) {

    operator fun invoke() = repository.getFavouriteArticlesFlow()
}