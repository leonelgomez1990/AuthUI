package com.leo.authui.menu.framework

import com.leo.authui.menu.framework.entities.NewsApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "fe050c83e88a4c9d93e6bff7842a1da1"

/*
Es el encargado de hacer peticiones a la REST API de newsapi.org
Convierte el JSON a una lista de objetos tipo News
El NewsProvider solamente se comunica con el NewsRepository
 */
interface NewsProvider {

    @GET("top-headlines?apiKey=$API_KEY")
    suspend fun topHeadLines(@Query("country") country: String): Response<NewsApiResponse>
}