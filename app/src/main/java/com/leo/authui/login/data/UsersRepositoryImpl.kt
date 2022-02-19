package com.leo.authui.login.data

import com.leo.authui.core.utils.MyResult
import com.leo.authui.login.framework.UsersDataSource

class UsersRepositoryImpl(
    private val usersDataSource: UsersDataSource
) : UsersRepository {

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): MyResult<Boolean> = usersDataSource.loginWithEmailAndPassword(email, password)

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): MyResult<Boolean> = usersDataSource.createUserWithEmailAndPassword(email, password)
}