package com.leo.authui.menu.data

import com.leo.authui.core.utils.MyResult
import com.leo.authui.menu.domain.News

interface NewsRepository {
    suspend fun getNews(country: String): MyResult<List<News>>
    suspend fun getNew(title: String): MyResult<News>
    suspend fun deleteNew(uid: String): MyResult<Boolean>
    suspend fun updateNew(data: News): MyResult<Boolean>

}