package com.vladusecho.xnews.data.remote

import com.google.gson.annotations.SerializedName

data class RequestDto(
    @SerializedName("articles") val articles: List<ArticleDto>
)