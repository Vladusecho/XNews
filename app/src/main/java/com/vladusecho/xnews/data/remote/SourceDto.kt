package com.vladusecho.xnews.data.remote

import com.google.gson.annotations.SerializedName

data class SourceDto(
    @SerializedName("name") val name: String
)
