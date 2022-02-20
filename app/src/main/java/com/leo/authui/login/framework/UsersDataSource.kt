package com.leo.authui.login.framework

import com.leo.authui.core.utils.MyResult

interface UsersDataSource {
    suspend fun loginWithEmailAndPassword(email: String, password: String): MyResult<Boolean>
    suspend fun createUserWithEmailAndPassword(email: String, password: String): MyResult<Boolean>
    suspend fun sendPasswordResetEmail(email: String): MyResult<Boolean>
}