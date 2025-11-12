package com.vladusecho.xnews.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class RequestDto(
    @SerializedName("articles") val articles: List<ArticleDto>
)
