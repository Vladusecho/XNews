package com.vladusecho.xnews.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity("favourite_articles")
data class ArticleModel (
    @PrimaryKey val id: String,
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: Long,
    val source: String
)