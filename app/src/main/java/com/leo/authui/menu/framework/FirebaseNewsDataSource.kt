package com.leo.authui.menu.framework

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.leo.authui.core.utils.MyResult
import com.leo.authui.menu.domain.News
import com.leo.authui.menu.framework.entities.FirebaseNew
import com.leo.authui.menu.framework.entities.toNews
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class FirebaseNewsDataSource(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : NewsDataSource {

    private var news: List<News> = emptyList()

    override suspend fun getNews(country: String): MyResult<List<News>> {
        return try {
            val documents = db.collection("News")
                .whereEqualTo("enabled", true)
                .orderBy("title")
                .get().await()

            if (!documents.isEmpty) {
                val list = mutableListOf<News>()
                documents.forEach { document ->
                    Log.d("GetNews", "Item retrieved with uid ${document.id}")
                    list.add(document.toObject<FirebaseNew>().toNews())
                }
                news = list.toList()
                MyResult.Success(list)
            } else {
                MyResult.Success(mutableListOf<News>())
            }
        } catch (e: Exception) {
            Log.e("GetNews", "Exception thrown: ${e.message}")
            MyResult.Failure(e)
        }
    }

    override suspend fun getNew(title: String): MyResult<News> {
        return try {
            MyResult.Success(news.first { it.title == title })
        } catch (e: Exception) {
            MyResult.Failure(e)
        }
    }


}