package com.vladusecho.xnews.presentation.viewModel

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
import kotlinx.coroutines.flow.flowOf
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

    private val _articles = MutableStateFlow<Flow<List<Article>>>(flowOf())
    val favouriteArticles
        get() = _articles

    fun deleteFromFavourite(articleId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteFromFavouriteUseCase(articleId)
        }
    }

    fun getFavouriteArticles() {
        _state.value = FavouriteState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = FavouriteState.Content
            _articles.value = loadFavouriteArticlesFlowUseCase()
        }
    }
}