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

}