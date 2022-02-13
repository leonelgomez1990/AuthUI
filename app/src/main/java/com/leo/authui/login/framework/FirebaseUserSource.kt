package com.leo.authui.login.framework

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.leo.authui.core.utils.MyResult
import com.leo.authui.login.data.UsersDataSource
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class FirebaseUserSource @Inject constructor(
    private val auth : FirebaseAuth
) : UsersDataSource {

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): MyResult<Boolean> {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            MyResult.Success(true)
        } catch (e: Exception) {
            Log.e("FirebaseUserSource", "Exception thrown: ${e.message}")
            return MyResult.Failure(e)
        }
        return MyResult.Success(false)
    }
}