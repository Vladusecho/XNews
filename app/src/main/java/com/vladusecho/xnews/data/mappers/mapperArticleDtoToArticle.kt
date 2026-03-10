package com.vladusecho.xnews.data.mappers

import com.vladusecho.xnews.data.remote.ArticleDto
import com.vladusecho.xnews.domain.models.Article

fun mapperArticleDtoToArticle(articleDto: ArticleDto) =
    Article(
        id = articleDto.id,
        author = articleDto.author,
        title = articleDto.title,
        description = articleDto.description,
        url = articleDto.url,
        urlToImage = articleDto.urlToImage,
        publishedAt = articleDto.publishedAt.toArticleDateFormat(),
        source = articleDto.source.name
    )