package com.vladusecho.xnews.data.mappers

import java.text.SimpleDateFormat
import java.util.Locale

fun String.toArticleDateFormat(): Long {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    return formatter.parse(this)?.time ?: System.currentTimeMillis()
}