package com.leo.authui.login.data

import com.leo.authui.core.utils.MyResult

interface UsersRepository {
    suspend fun loginWithEmailAndPassword(email: String, password: String): MyResult<Boolean>
}