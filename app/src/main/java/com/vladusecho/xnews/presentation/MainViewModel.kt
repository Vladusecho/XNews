package com.vladusecho.xnews.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.xnews.data.repository.ArticlesRepositoryImpl
import com.vladusecho.xnews.domain.usecases.LoadArticlesUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = ArticlesRepositoryImpl
    private val loadArticlesUseCase = LoadArticlesUseCase(repository)

    private val _state = MutableStateFlow<MainState>(MainState.Initial)
    val state
        get() = _state.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _state.value = MainState.Error(throwable.message.toString())
    }

    fun loadArticles(query: String) {
        _state.value = MainState.Loading
        viewModelScope.launch(exceptionHandler) {
            _state.value = MainState.Content(
                loadArticlesUseCase(query)
            )
        }
    }
}