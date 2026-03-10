package com.vladusecho.xnews.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vladusecho.xnews.data.local.Dao
import com.vladusecho.xnews.data.local.FavouriteDatabase
import com.vladusecho.xnews.data.remote.ApiFactory
import com.vladusecho.xnews.data.remote.ApiService
import com.vladusecho.xnews.data.repository.ArticlesRepositoryImpl
import com.vladusecho.xnews.domain.repository.ArticlesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindArticlesRepository(impl: ArticlesRepositoryImpl): ArticlesRepository

    companion object {

        @Provides
        @Singleton
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }

        @Provides
        @Singleton
        fun provideDatabase(
            @ApplicationContext context: Context
        ): FavouriteDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = FavouriteDatabase::class.java,
                "FavouriteDB"
            ).build()
        }

        @Provides
        @Singleton
        fun provideDao(database: FavouriteDatabase): Dao {
            return database.dao()
        }
    }
}