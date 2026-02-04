package com.vladusecho.xnews.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.xnews.domain.models.Article
import com.vladusecho.xnews.domain.repository.ArticlesRepository
import com.vladusecho.xnews.domain.usecases.DeleteFromFavouriteUseCase
import com.vladusecho.xnews.domain.usecases.LoadFavouriteArticlesFlowUseCase
import com.vladusecho.xnews.domain.usecases.LoadFavouriteArticlesUseCase
import com.vladusecho.xnews.presentation.state.FavouriteState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouriteViewModel @Inject constructor(
    private val deleteFromFavouriteUseCase: DeleteFromFavouriteUseCase,
    private val loadFavouriteArticlesUseCase: LoadFavouriteArticlesUseCase,
    private val loadFavouriteArticlesFlowUseCase: LoadFavouriteArticlesFlowUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<FavouriteState>(FavouriteState.Initial)
    val state
        get() = _state

    val articles: Flow<List<Article>> = loadFavouriteArticlesFlowUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        _state.value = FavouriteState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = FavouriteState.Content(loadFavouriteArticlesUseCase())
        }
    }

    fun deleteFromFavourite(articleId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteFromFavouriteUseCase(articleId)
        }
    }

//    fun getFavouriteArticles() {
//        _state.value = FavouriteState.Loading
//        viewModelScope.launch(Dispatchers.IO) {
//            _state.value = FavouriteState.Content
//            _articles.value = loadFavouriteArticlesFlowUseCase()
//        }
//    }
}