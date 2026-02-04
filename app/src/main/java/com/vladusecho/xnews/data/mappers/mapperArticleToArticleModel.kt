package com.vladusecho.xnews.data.mappers

import com.vladusecho.xnews.data.local.ArticleModel
import com.vladusecho.xnews.domain.models.Article

fun Article.mapperArticleToArticleModel() = ArticleModel(
    id, author, title, description, url, urlToImage, publishedAt, content
)