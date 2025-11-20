package com.vladusecho.xnews.domain.models

import java.time.Instant

data class Article(
    val id: Int,
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String
) {

    val date: Instant
        get() = Instant.parse(publishedAt)

    fun getDate() = publishedAt.split("T")[0]

    fun getDateWithAuthor() = getDate() + "  /  " + (author?.uppercase() ?: "")

}