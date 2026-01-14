package com.vladusecho.xnews.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.xnews.data.repository.ArticlesRepositoryImpl
import com.vladusecho.xnews.domain.usecases.LoadArticlesUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class MainViewModel : ViewModel() {
    private val repository = ArticlesRepositoryImpl
    private val loadArticlesUseCase = LoadArticlesUseCase(repository)

    private val _state = MutableStateFlow<MainState>(MainState.Initial)
    val state
        get() = _state.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _state.value = MainState.Error(throwable.message.toString())
    }

    private val searchQueryChannel = Channel<String>(Channel.CONFLATED)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery
        get() = _searchQuery.asStateFlow()


    init {
        viewModelScope.launch {
            searchQueryChannel.receiveAsFlow()
                .debounce(600)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotBlank()) {
                        performSearch(query)
                    } else {
                        _state.value = MainState.Initial
                    }
                }
        }
    }

    fun performSearch(query: String) {
        _state.value = MainState.Loading
        viewModelScope.launch(exceptionHandler) {
            _state.value = MainState.Content(
                loadArticlesUseCase(query)
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            searchQueryChannel.send(query)
        }
    }
}