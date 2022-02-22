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
        title: String
    ): MyResult<News> = newsDataSource.getNew(title)

    override suspend fun deleteNew(new: String): MyResult<Boolean> {
        return newsDataSource.deleteNew(new)
    }

    override suspend fun updateNew(data: News): MyResult<Boolean> {
        return newsDataSource.updateNew(data)
    }

}