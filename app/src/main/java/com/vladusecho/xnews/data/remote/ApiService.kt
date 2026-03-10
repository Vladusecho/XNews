package com.vladusecho.xnews.data.remote

import com.vladusecho.xnews.data.remote.ArticleDto
import com.vladusecho.xnews.data.remote.RequestDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("everything?language=ru&sortBy=publishedAt")
    suspend fun getArticles(
        @Query("q") query: String
    ): RequestDto

    @GET("everything?language=ru&sortBy=publishedAt&pageSize=4")
    suspend fun getFourMainArticles(
        @Query("q") query: String
    ): RequestDto
}