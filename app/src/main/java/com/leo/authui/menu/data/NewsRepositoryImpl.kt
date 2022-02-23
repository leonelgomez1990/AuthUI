package com.leo.authui.menu.data

import com.leo.authui.core.utils.MyResult
import com.leo.authui.menu.domain.News
import com.leo.authui.menu.framework.NewsDataSource

class NewsRepositoryImpl(
    private val newsDataSource: NewsDataSource
) : NewsRepository {

    override suspend fun getNews(
        country: String
    ): MyResult<List<News>> = newsDataSource.getNews(country)

    override suspend fun getNew(
        uid: String
    ): MyResult<News> = newsDataSource.getNew(uid)

    override suspend fun deleteNew(uid: String): MyResult<Boolean> {
        return newsDataSource.deleteNew(uid)
    }

    override suspend fun updateNew(data: News): MyResult<Boolean> {
        return newsDataSource.updateNew(data)
    }

}