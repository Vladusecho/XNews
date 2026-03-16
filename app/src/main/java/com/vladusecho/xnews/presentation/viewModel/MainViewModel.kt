package com.vladusecho.xnews.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.xnews.domain.models.Article
import com.vladusecho.xnews.domain.usecases.AddToFavouriteUseCase
import com.vladusecho.xnews.domain.usecases.LoadArticlesUseCase
import com.vladusecho.xnews.domain.usecases.LoadSomeMainArticlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val loadArticlesUseCase: LoadArticlesUseCase,
    private val loadSomeMainArticlesUseCase: LoadSomeMainArticlesUseCase,
    private val addToFavouriteUseCase: AddToFavouriteUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState.Initial)
    val state = _state.asStateFlow()

    val lastIndex = MutableStateFlow("-1")

    val isLoadingMore = MutableStateFlow(false)

    val page = MutableStateFlow(2)

    val mainContent = MutableStateFlow(mutableListOf<MainContent>())

    init {
        viewModelScope.launch {
            _state.value = MainState.Loading
            val hotArticles = loadSomeMainArticlesUseCase("Россия", 10)
            mainContent.update { prev ->
                (prev + MainContent(
                    title = "Горячие новости",
                    articles = hotArticles,
                    isRow = true
                )) as MutableList<MainContent>
            }
            _state.value = MainState.Content(mainContent.value)

            val politicArticles = loadSomeMainArticlesUseCase("Политика", 4)
            mainContent.update { prev ->
                (prev + MainContent(
                    title = "Политика",
                    articles = politicArticles
                )) as MutableList<MainContent>
            }

            val economicArticles = loadSomeMainArticlesUseCase("Экономика", 4)
            mainContent.update { prev ->
                (prev + MainContent(
                    title = "Экономика",
                    articles = economicArticles
                )) as MutableList<MainContent>
            }
            _state.value = MainState.Content(mainContent.value)

            val scienceArticles = loadSomeMainArticlesUseCase("Наука", 4)
            mainContent.update { prev ->
                (prev + MainContent(
                    title = "Наука",
                    articles = scienceArticles
                )) as MutableList<MainContent>
            }
            _state.value = MainState.Content(mainContent.value)

            val othersArticles = loadSomeMainArticlesUseCase("Новости", 10)
            mainContent.update { prev ->
                (prev + MainContent(
                    title = "Другие новости",
                    articles = othersArticles,
                    isInfinityColumn = true
                )) as MutableList<MainContent>
            }
            lastIndex.value = othersArticles.last().id
            _state.value = MainState.Content(mainContent.value)
        }
    }

    fun processCommand(mainCommand: MainCommand) {
        when (mainCommand) {
            is MainCommand.LoadOthersNews -> {
                viewModelScope.launch {
                    if (!isLoadingMore.value) {
                        Log.d("LaunchedEffect", "view model work")
                        isLoadingMore.value = true
                        val othersNews = loadSomeMainArticlesUseCase("Новости", 10, page.value)
                        delay(1000)
                        mainContent.update { prev ->
                            (prev + MainContent(
                                title = "Новости",
                                articles = othersNews,
                                isInfinityColumn = true,
                                isTitleInvisible = true
                            )) as MutableList<MainContent>
                        }
                        lastIndex.value = othersNews.last().id
                        _state.value = MainState.Content(mainContent.value)
                        page.value++
                        isLoadingMore.value = false
                    }
                }
            }
        }
    }


//    private val _state = MutableStateFlow<MainState>(MainState.Initial)
//    val state
//        get() = _state.asStateFlow()
//
//    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
//        _state.value = MainState.Error(throwable.message.toString())
//    }
//
//    private val searchQueryChannel = Channel<String>(Channel.Factory.CONFLATED)
//
//    private val _searchQuery = MutableStateFlow("")
//    val searchQuery
//        get() = _searchQuery.asStateFlow()
//
//
//    private val _unseenNews = MutableStateFlow(0)
//    val unseenNews
//        get() = _unseenNews.asStateFlow()
//
//    private val _isWatchedFavourite = MutableStateFlow(false)
//    val isWatchedFavourite
//        get() = _isWatchedFavourite.asStateFlow()
//
//    private val _isDuplicate = MutableStateFlow(false)
//    val isDuplicate
//        get() = _isDuplicate.asStateFlow()
//
//    fun incrementNewFavouriteCount() {
//        _unseenNews.value = _unseenNews.value + 1
//    }
//
//    fun invisibleCounter() {
//        _isWatchedFavourite.value = true
//    }
//
//    fun visibleCounter() {
//        _unseenNews.value = 0
//        _isWatchedFavourite.value = false
//    }
//
//    init {
//        _state.value = MainState.Loading
//        viewModelScope.launch {
//            _state.value = MainState.MainNews(loadArticlesUseCase(INIT_QUERY))
//            searchQueryChannel.receiveAsFlow()
//                .debounce(600)
//                .distinctUntilChanged()
//                .collect { query ->
//                    if (query.isNotBlank()) {
//                        performSearch(query)
//                    } else {
//                        _state.value = MainState.Loading
//                        _state.value = MainState.MainNews(loadArticlesUseCase(INIT_QUERY))
//                    }
//                }
//        }
//    }
//
//    fun performSearch(query: String) {
//        _state.value = MainState.Loading
//        viewModelScope.launch(exceptionHandler) {
//            _state.value = MainState.Content(
//                loadArticlesUseCase(query)
//            )
//        }
//    }
//
//    fun onSearchQueryChanged(query: String) {
//        _searchQuery.value = query
//        viewModelScope.launch {
//            searchQueryChannel.send(query)
//        }
//    }
//
//    fun addToFavourite(article: Article): Deferred<Duplicate> = viewModelScope.async(Dispatchers.IO) {
//            val urls = checkDuplicatesUseCase()
//            if (article.url !in urls) {
//                addToFavouriteUseCase(article)
//                return@async Duplicate.False
//            } else {
//                return@async Duplicate.True
//            }
//        }
//
//    private companion object {
//
//        private const val INIT_QUERY = "Россия"
//    }
//
//    sealed class Duplicate {
//
//        object True : Duplicate()
//
//        object False : Duplicate()
//    }
}

sealed interface MainCommand {

    data object LoadOthersNews : MainCommand
}

sealed interface MainState {

    data object Initial : MainState

    data object Loading : MainState

    data class Error(val error: String) : MainState

    data class Content(
        val content: List<MainContent>
    ) : MainState
}

data class MainContent(
    val articles: List<Article>,
    val title: String,
    val isRow: Boolean = false,
    val isInfinityColumn: Boolean = false,
    val isTitleInvisible: Boolean = false
)
