package com.leo.authui.menu.framework

import com.leo.authui.core.utils.MyResult
import com.leo.authui.menu.domain.News

interface NewsDataSource {
    suspend fun getNews(country: String): MyResult<List<News>>
    suspend fun getNew(uid: String): MyResult<News>
    suspend fun deleteNew(uid: String): MyResult<Boolean>
    suspend fun updateNew(data: News): MyResult<Boolean>
    suspend fun createNew(data: News): MyResult<String>
}