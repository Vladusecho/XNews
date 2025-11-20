package com.vladusecho.xnews.data.models

import androidx.compose.ui.text.toUpperCase
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.StringFormat

data class ArticleDto(
    @SerializedName("id") val id: Int,
    @SerializedName("author") val author: String?,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("url") val url: String,
    @SerializedName("urlToImage") val urlToImage: String,
    @SerializedName("publishedAt") val publishedAt: String,
    @SerializedName("content") val content: String
) {

    fun getDate() = publishedAt.split("T")[0]

    fun getDateWithAuthor() = getDate() + "  /  " + (author?.uppercase() ?: "")
}
