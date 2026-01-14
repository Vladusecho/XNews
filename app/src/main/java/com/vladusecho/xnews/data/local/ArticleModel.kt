package com.vladusecho.xnews.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity("favourite_articles")
class ArticleModel (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String
)