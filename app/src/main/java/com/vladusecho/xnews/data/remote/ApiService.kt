package com.vladusecho.xnews.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("everything?language=ru&sortBy=publishedAt")
    suspend fun getArticles(
        @Query("q") query: String
    ): RequestDto

    @GET("everything?language=ru&sortBy=publishedAt")
    suspend fun getSomeMainArticles(
        @Query("q") query: String,
        @Query("pageSize") count: Int,
        @Query("page") page: Int
    ): RequestDto
}