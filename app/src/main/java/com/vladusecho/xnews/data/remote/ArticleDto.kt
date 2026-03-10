package com.vladusecho.xnews.data.remote

import com.google.gson.annotations.SerializedName

data class ArticleDto(
    @SerializedName("id") val id: Int,
    @SerializedName("author") val author: String?,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("url") val url: String,
    @SerializedName("urlToImage") val urlToImage: String?,
    @SerializedName("publishedAt") val publishedAt: String,
    @SerializedName("source") val source: SourceDto
)