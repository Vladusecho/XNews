package com.vladusecho.xnews.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.xnews.domain.models.Article
import com.vladusecho.xnews.domain.usecases.LoadSomeMainArticlesUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(
    assistedFactory = MoreArticlesViewModel.Factory::class
)
class MoreArticlesViewModel @AssistedInject constructor(
    private val loadSomeMainArticlesUseCase: LoadSomeMainArticlesUseCase,
    @Assisted("title") private val title: String
) : ViewModel() {

    private val _state = MutableStateFlow<MoreArticlesState>(MoreArticlesState.Initial)
    val state = _state.asStateFlow()

    private val articlesList = MutableStateFlow(listOf<Article>())

    private val isLoadingMore = MutableStateFlow(false)

    private val page = MutableStateFlow(NEXT_PAGE)

    private val _lastIndex = MutableStateFlow(INIT_LIST_INDEX)
    val lastIndex = _lastIndex.asStateFlow()

    fun processCommand(command: MoreArticlesCommand) {
        when (command) {
            MoreArticlesCommand.LoadOthersNews -> {
                viewModelScope.launch {
                    if (!isLoadingMore.value) {
                        isLoadingMore.value = true
                        val othersNews = loadSomeMainArticlesUseCase(title, 10, page.value)
                        articlesList.update { prev ->
                            prev + othersNews
                        }
                        _lastIndex.value = othersNews.last().id
                        _state.value = MoreArticlesState.Content(articlesList.value)
                        page.value++
                        isLoadingMore.value = false
                    }
                }
            }
        }
    }

    init {
        loadArticles()
    }

    fun loadArticles() {
        viewModelScope.launch {
            _state.value = MoreArticlesState.Loading
            val articles = loadSomeMainArticlesUseCase(query = title, count = 10)
            articlesList.update { prev ->
                prev + articles
            }
            _lastIndex.value = articles.last().id
            _state.value = MoreArticlesState.Content(articlesList.value)
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("title") title: String
        ): MoreArticlesViewModel
    }

    private companion object {

        const val NEXT_PAGE = 2

        const val INIT_LIST_INDEX = "-1"
    }
}

sealed interface MoreArticlesState {

    object Initial : MoreArticlesState

    object Loading : MoreArticlesState

    data class Content(
        val articlesList: List<Article>
    ) : MoreArticlesState
}

sealed interface MoreArticlesCommand {

    data object LoadOthersNews : MoreArticlesCommand
}