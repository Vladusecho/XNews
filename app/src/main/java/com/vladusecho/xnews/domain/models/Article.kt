package com.vladusecho.xnews.domain.models

data class Article(
    val id: String,
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: Long,
    val source: String?
)