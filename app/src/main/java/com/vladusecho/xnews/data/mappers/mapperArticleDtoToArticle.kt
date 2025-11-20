package com.vladusecho.xnews.data.mappers

import com.vladusecho.xnews.data.models.ArticleDto
import com.vladusecho.xnews.domain.models.Article

fun mapperArticleDtoToArticle(articleDto: ArticleDto) =
    Article(
        articleDto.id,
        articleDto.author,
        articleDto.title,
        articleDto.description,
        articleDto.url,
        articleDto.urlToImage,
        articleDto.publishedAt,
        articleDto.content
    )