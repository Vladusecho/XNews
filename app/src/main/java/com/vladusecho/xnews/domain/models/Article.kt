package com.vladusecho.xnews.domain.models

data class Article(
    val id: Int,
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: Long,
    val source: String?
)