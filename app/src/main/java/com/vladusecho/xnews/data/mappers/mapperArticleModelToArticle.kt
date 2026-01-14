package com.vladusecho.xnews.data.mappers

import com.vladusecho.xnews.data.local.ArticleModel
import com.vladusecho.xnews.domain.models.Article

fun ArticleModel.mapperArticleModelToArticle() = Article(
    id, author, title, description, url, urlToImage, publishedAt, content
)