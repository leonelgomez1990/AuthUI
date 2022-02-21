package com.leo.authui.menu.framework

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.leo.authui.core.utils.MyResult
import com.leo.authui.menu.domain.News
import com.leo.authui.menu.framework.entities.toFirebaseNew
import kotlinx.coroutines.tasks.await
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
            //uploadToFirebase()

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

    override suspend fun deleteNew(uid: String): MyResult<Boolean> {
        return MyResult.Success(false)
    }

    private suspend fun uploadToFirebase() {
        val db = Firebase.firestore

        news.forEach { new ->
            val document = db.collection("News").document()
            new.uid = document.id
            new.enabled = true
            val query = document
            try{
                val data = query
                    .set(new.toFirebaseNew())
                    .await()
            }catch(e: java.lang.Exception){
            }
        }


    }


}

class MissingApiKeyException : java.lang.Exception()
class ApiKeyInvalidException : java.lang.Exception()