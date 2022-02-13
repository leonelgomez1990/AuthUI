package com.leo.authui.login.data

import com.leo.authui.core.utils.MyResult
import javax.inject.Inject

class UsersRepository @Inject constructor (
    private val userDataSource : UsersDataSource
){
    suspend fun loginWithEmailAndPassword(email: String, password: String): MyResult<Boolean> {
        return userDataSource.loginWithEmailAndPassword(email, password)
    }
}
