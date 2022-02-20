package com.leo.authui.menu.framework

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.leo.authui.core.utils.MyResult
import com.leo.authui.menu.domain.News

class FirebaseNewsDataSource(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : NewsDataSource {
    override suspend fun getNews(country: String): MyResult<List<News>> {
        TODO("Not yet implemented")
    }

    override suspend fun getNew(title: String): MyResult<News> {
        TODO("Not yet implemented")
    }


}