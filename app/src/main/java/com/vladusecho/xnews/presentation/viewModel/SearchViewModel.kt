package com.vladusecho.xnews.presentation.viewModel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.xnews.domain.models.Article
import com.vladusecho.xnews.domain.usecases.LoadSomeMainArticlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val loadSomeMainArticlesUseCase: LoadSomeMainArticlesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Initial)
    val state = _state.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    init {
        viewModelScope.launch {
            _query
                .debounce(600)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isNotBlank()) {
                        performSearch(query)
                    } else {
                        _state.value = SearchState.Initial
                    }
                }
        }
    }

    private suspend fun performSearch(query: String) {
        _state.value = SearchState.Loading
        try {
            val articles = loadSomeMainArticlesUseCase(query = query, count = 10)
            if (articles.isEmpty()) _state.value = SearchState.Initial else _state.value = SearchState.Content(articles)
        } catch (e: Exception) {
            _state.value = SearchState.Error(e.message ?: "Ошибка поиска")
        }
    }

    fun processCommand(command: SearchCommand) {
        when(command) {
            is SearchCommand.InputQuery -> {
                _query.value = command.query
            }
        }
    }
}

sealed interface SearchState {

    object Initial : SearchState
    object Loading : SearchState
    data class Content(
        val articles: List<Article>
    ) : SearchState
    data class Error(
        val error: String
    ) : SearchState
}

sealed interface SearchCommand {

    data class InputQuery(
        val query: String
    ) : SearchCommand
}