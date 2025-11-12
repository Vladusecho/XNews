package com.vladusecho.xnews.data.remote

import com.vladusecho.xnews.data.models.ArticleDto
import com.vladusecho.xnews.data.models.RequestDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("everything?language=ru")
    suspend fun getArticles(
        @Query("q") query: String
    ): RequestDto
}