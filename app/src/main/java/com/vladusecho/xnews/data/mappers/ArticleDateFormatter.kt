package com.vladusecho.xnews.data.mappers

import kotlin.time.Instant

fun String.toArticleDateFormat(): Long {
    val instant = Instant.parse(this)
    return instant.toEpochMilliseconds()
}