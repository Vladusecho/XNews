package com.vladusecho.xnews.domain.usecases

import com.vladusecho.xnews.domain.repository.ArticlesRepository
import javax.inject.Inject

class CheckDuplicatesUseCase @Inject constructor(
    private val repository: ArticlesRepository
) {

    suspend operator fun invoke(): List<String> {
        return repository.checkDuplicates()
    }
}