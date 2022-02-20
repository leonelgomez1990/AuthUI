package com.leo.authui.login.framework

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.leo.authui.core.utils.MyResult
import com.leo.authui.login.data.UsersRepository
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class FirebaseUserSource constructor(
    private val auth: FirebaseAuth
) : UsersDataSource {

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): MyResult<Boolean> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Log.d("FirebaseUserSource", "signInWithEmail:success")
            MyResult.Success(true)
        } catch (e: Exception) {
            Log.w("FirebaseUserSource", "signInWithEmail:failure, Exception thrown: ${e.message}")
            MyResult.Failure(e)
        }
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): MyResult<Boolean> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Log.d("FirebaseUserSource", "createUserWithEmailAndPassword:success")
            MyResult.Success(true)
        } catch (e: Exception) {
            Log.w(
                "FirebaseUserSource",
                "createUserWithEmailAndPassword:failure, Exception thrown: ${e.message}"
            )
            MyResult.Failure(e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): MyResult<Boolean> {
        return try {
            val result = auth.sendPasswordResetEmail(email).await()
            Log.d("FirebaseUserSource", "sendPasswordResetEmail:success")
            MyResult.Success(true)
        } catch (e: Exception) {
            Log.w(
                "FirebaseUserSource",
                "sendPasswordResetEmail:failure, Exception thrown: ${e.message}"
            )
            MyResult.Failure(e)
        }
    }
}