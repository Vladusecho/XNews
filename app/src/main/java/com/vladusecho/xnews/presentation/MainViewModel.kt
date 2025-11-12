package com.vladusecho.xnews.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladusecho.xnews.data.remote.ApiFactory
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    fun loadArticles(query: String) {
        viewModelScope.launch {
            ApiFactory.apiService.getArticles(query)
        }
    }
}