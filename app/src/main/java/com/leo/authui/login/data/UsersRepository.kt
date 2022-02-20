package com.leo.authui.login.data

import com.leo.authui.core.utils.MyResult

interface UsersRepository {
    suspend fun loginWithEmailAndPassword(email: String, password: String): MyResult<Boolean>
    suspend fun createUserWithEmailAndPassword(email: String, password: String): MyResult<Boolean>
    suspend fun sendPasswordResetEmail(email: String): MyResult<Boolean>
}