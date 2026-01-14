package com.vladusecho.xnews.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToFavourite(articleModel: ArticleModel)

    @Query("DELETE FROM favourite_articles WHERE id=:id")
    suspend fun deleteFromFavourite(id: Int)

    @Query("SELECT * FROM favourite_articles")
    fun getFavouriteArticles(): Flow<List<ArticleModel>>
}