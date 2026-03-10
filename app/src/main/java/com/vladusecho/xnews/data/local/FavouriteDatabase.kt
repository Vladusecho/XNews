package com.vladusecho.xnews.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.hilt.android.qualifiers.ApplicationContext

@Database(
    entities = [ArticleModel::class],
    version = 2,
    exportSchema = false
)
abstract class FavouriteDatabase : RoomDatabase() {

    abstract fun dao(): Dao
}