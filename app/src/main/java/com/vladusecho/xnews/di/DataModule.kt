package com.vladusecho.xnews.di

import android.content.Context
import com.vladusecho.xnews.data.local.Dao
import com.vladusecho.xnews.data.local.FavouriteDatabase
import com.vladusecho.xnews.data.remote.ApiFactory
import com.vladusecho.xnews.data.remote.ApiService
import com.vladusecho.xnews.data.repository.ArticlesRepositoryImpl
import com.vladusecho.xnews.domain.repository.ArticlesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindArticlesRepository(impl: ArticlesRepositoryImpl): ArticlesRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }

        @ApplicationScope
        @Provides
        fun provideDatabase(context: Context): FavouriteDatabase {
            return FavouriteDatabase.getInstance(context = context)
        }

        @ApplicationScope
        @Provides
        fun provideDao(database: FavouriteDatabase): Dao {
            return database.dao()
        }
    }
}