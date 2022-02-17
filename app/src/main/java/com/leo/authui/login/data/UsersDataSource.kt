package com.leo.authui.login.data

import com.leo.authui.core.utils.MyResult

interface UsersDataSource {
    suspend fun loginWithEmailAndPassword(email: String, password: String): MyResult<Boolean>
}