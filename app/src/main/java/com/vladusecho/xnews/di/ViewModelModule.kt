package com.vladusecho.xnews.di

import androidx.lifecycle.ViewModel
import com.vladusecho.xnews.presentation.viewModel.FavouriteViewModel
import com.vladusecho.xnews.presentation.viewModel.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(MainViewModel::class)
    @Binds
    fun bindMainViewModel(impl: MainViewModel): ViewModel

    @IntoMap
    @ViewModelKey(FavouriteViewModel::class)
    @Binds
    fun bindFavouriteViewModel(impl: FavouriteViewModel): ViewModel
}