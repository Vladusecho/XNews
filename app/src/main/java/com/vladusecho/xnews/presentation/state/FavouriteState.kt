package com.vladusecho.xnews.presentation.state

import com.vladusecho.xnews.domain.models.Article
import kotlinx.coroutines.flow.Flow

sealed class FavouriteState {

    object Initial : FavouriteState()

    object Loading : FavouriteState()

    object Content : FavouriteState()
}