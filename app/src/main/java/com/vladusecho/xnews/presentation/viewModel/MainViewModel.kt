package com.vladusecho.xnews.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.xnews.domain.models.Article
import com.vladusecho.xnews.domain.usecases.LoadSomeMainArticlesUseCase
import com.vladusecho.xnews.presentation.viewModel.MainState.Content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val loadSomeMainArticlesUseCase: LoadSomeMainArticlesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState.Initial)
    val state = _state.asStateFlow()

    val time = MutableStateFlow(System.currentTimeMillis())

    val lastIndex = MutableStateFlow("-1")

    val isLoadingMore = MutableStateFlow(false)

    val page = MutableStateFlow(2)

    val mainContent = MutableStateFlow(mutableListOf<MainContent>())

    init {
        loadArticles()
    }

    private fun loadArticles() {
        Log.d("viewmodel", "work")
        viewModelScope.launch {
            mainContent.value = mutableListOf()
            _state.value = MainState.Loading
            val hotArticles = loadSomeMainArticlesUseCase("Россия", 10)
            mainContent.update { prev ->
                (prev + MainContent(
                    title = "Горячие новости",
                    articles = hotArticles,
                    isRow = true
                )) as MutableList<MainContent>
            }
            _state.value = Content(mainContent.value)

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
            _state.value = Content(mainContent.value)

            val scienceArticles = loadSomeMainArticlesUseCase("Наука", 4)
            mainContent.update { prev ->
                (prev + MainContent(
                    title = "Наука",
                    articles = scienceArticles
                )) as MutableList<MainContent>
            }
            _state.value = Content(mainContent.value)

            val othersArticles = loadSomeMainArticlesUseCase("Новости", 10)
            mainContent.update { prev ->
                (prev + MainContent(
                    title = "Другие новости",
                    articles = othersArticles,
                    isInfinityColumn = true
                )) as MutableList<MainContent>
            }
            lastIndex.value = othersArticles.last().id
            _state.value = Content(mainContent.value)
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
                        mainContent.update { prev ->
                            (prev + MainContent(
                                title = "Новости",
                                articles = othersNews,
                                isInfinityColumn = true,
                                isTitleInvisible = true
                            )) as MutableList<MainContent>
                        }
                        lastIndex.value = othersNews.last().id
                        _state.value = Content(mainContent.value)
                        page.value++
                        isLoadingMore.value = false
                    }
                }
            }

            MainCommand.RefreshNews -> {
                loadArticles()
                page.value = 2
                time.value = System.currentTimeMillis()
            }
        }
    }
}

sealed interface MainCommand {

    data object LoadOthersNews : MainCommand

    data object RefreshNews : MainCommand
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
