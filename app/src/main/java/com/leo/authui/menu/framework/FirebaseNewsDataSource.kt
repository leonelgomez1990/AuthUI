package com.leo.authui.menu.framework

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.leo.authui.core.utils.MyResult
import com.leo.authui.menu.domain.News
import com.leo.authui.menu.framework.entities.FirebaseNew
import com.leo.authui.menu.framework.entities.toFirebaseNew
import com.leo.authui.menu.framework.entities.toNews
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class FirebaseNewsDataSource(
    private val db: FirebaseFirestore
) : NewsDataSource {

    private var news: List<News> = emptyList()
    private val COLLECTIONPATH = "News"

    override suspend fun getNews(country: String): MyResult<List<News>> {
        return try {
            val documents = db.collection(COLLECTIONPATH)
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

    override suspend fun getNew(uid: String): MyResult<News> {
        return try {
            MyResult.Success(news.first { it.uid == uid })
        } catch (e: Exception) {
            MyResult.Failure(e)
        }
    }

    override suspend fun deleteNew(uid: String): MyResult<Boolean> {
        return try {
            val document = db.collection(COLLECTIONPATH).document(uid)
            val data = document
                .delete()
                .await()

            MyResult.Success(true)
        } catch (e: Exception) {
            Log.e("deleteNew", "Exception thrown: ${e.message}")
            MyResult.Failure(e)
        }
    }

    override suspend fun updateNew(data: News): MyResult<Boolean> {
        if (data.uid == null) {
            Log.e("updateNew", "Exception thrown: uid is null")
            MyResult.Failure(Exception("Error"))
        }
        val document = db.collection(COLLECTIONPATH).document(data.uid!!)
        return try {
            val op = document
                .set(data.toFirebaseNew())
                .await()
            MyResult.Success(true)
        } catch (e: Exception) {
            Log.e("updateNew", "Exception thrown: ${e.message}")
            MyResult.Failure(e)
        }
    }

    override suspend fun createNew(data: News): MyResult<String> {
        val document = db.collection(COLLECTIONPATH).document()
        data.uid = document.id
        return try {
            val op = document
                .set(data.toFirebaseNew())
                .await()
            MyResult.Success(document.id)
        } catch (e: Exception) {
            Log.e("createNew", "Exception thrown: ${e.message}")
            MyResult.Failure(e)
        }
    }

}