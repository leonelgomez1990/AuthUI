package com.leo.authui.menu.framework

import com.leo.authui.core.utils.MyResult
import com.leo.authui.menu.domain.News
import javax.inject.Inject

class NewsProviderImpl @Inject constructor(
    private val newsProvider: NewsProvider
): NewsDataSource {

    private var news: List<News> = emptyList()

    override suspend fun getNews(country: String): MyResult<List<News>> {
        return try {
            val apiResponse = newsProvider.topHeadLines(country).body()
            if (apiResponse?.status == "error") {
                when (apiResponse.code) {
                    "apiKeyMissing" -> throw MissingApiKeyException()
                    "apiKeyInvalid" -> throw ApiKeyInvalidException()
                    else -> throw Exception()
                }
            }
            news = apiResponse?.articles ?: emptyList()

            MyResult.Success(news)
        }
        catch (e : Exception) {
            MyResult.Failure(e)
        }

    }

    override suspend fun getNew(title: String): MyResult<News> {
        return try {
            MyResult.Success(news.first { it.title == title })
        }
        catch (e : Exception) {
            MyResult.Failure(e)
        }
    }


}

class MissingApiKeyException : java.lang.Exception()
class ApiKeyInvalidException : java.lang.Exception()