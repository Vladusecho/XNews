package com.vladusecho.xnews.data.mappers

import com.vladusecho.xnews.data.local.ArticleModel

fun List<ArticleModel>.mapperArticleModelsToArticle() = this.map { it.mapperArticleModelToArticle() }