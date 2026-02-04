package com.vladusecho.xnews.presentation.viewModel

import android.text.BoringLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.xnews.domain.models.Article
import com.vladusecho.xnews.domain.usecases.AddToFavouriteUseCase
import com.vladusecho.xnews.domain.usecases.CheckDuplicatesUseCase
import com.vladusecho.xnews.domain.usecases.LoadArticlesUseCase
import com.vladusecho.xnews.presentation.state.MainState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
class MainViewModel @Inject constructor(
    private val loadArticlesUseCase: LoadArticlesUseCase,
    private val addToFavouriteUseCase: AddToFavouriteUseCase,
    private val checkDuplicatesUseCase: CheckDuplicatesUseCase
) : ViewModel() {


    private val _state = MutableStateFlow<MainState>(MainState.Initial)
    val state
        get() = _state.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _state.value = MainState.Error(throwable.message.toString())
    }

    private val searchQueryChannel = Channel<String>(Channel.Factory.CONFLATED)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery
        get() = _searchQuery.asStateFlow()


    private val _unseenNews = MutableStateFlow(0)
    val unseenNews
        get() = _unseenNews.asStateFlow()

    private val _isWatchedFavourite = MutableStateFlow(false)
    val isWatchedFavourite
        get() = _isWatchedFavourite.asStateFlow()

    private val _isDuplicate = MutableStateFlow(false)
    val isDuplicate
        get() = _isDuplicate.asStateFlow()

    fun incrementNewFavouriteCount() {
        _unseenNews.value = _unseenNews.value + 1
    }

    fun invisibleCounter() {
        _isWatchedFavourite.value = true
    }

    fun visibleCounter() {
        _unseenNews.value = 0
        _isWatchedFavourite.value = false
    }

    init {
        _state.value = MainState.Loading
        viewModelScope.launch {
            _state.value = MainState.MainNews(loadArticlesUseCase(INIT_QUERY))
            searchQueryChannel.receiveAsFlow()
                .debounce(600)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotBlank()) {
                        performSearch(query)
                    } else {
                        _state.value = MainState.Loading
                        _state.value = MainState.MainNews(loadArticlesUseCase(INIT_QUERY))
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

    fun addToFavourite(article: Article): Deferred<Duplicate> = viewModelScope.async(Dispatchers.IO) {
            val urls = checkDuplicatesUseCase()
            if (article.url !in urls) {
                addToFavouriteUseCase(article)
                return@async Duplicate.False
            } else {
                return@async Duplicate.True
            }
        }

    private companion object {

        private const val INIT_QUERY = "Россия"
    }

    sealed class Duplicate {

        object True : Duplicate()

        object False : Duplicate()
    }
}