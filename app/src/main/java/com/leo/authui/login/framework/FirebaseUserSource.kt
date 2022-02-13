package com.leo.authui.login.framework

import com.leo.authui.core.utils.MyResult
import com.leo.authui.login.data.UsersDataSource
import java.lang.Exception
import javax.inject.Inject

class FirebaseUserSource @Inject constructor() :
    UsersDataSource{
    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): MyResult<Boolean> {
        return if(password == "gomez")
            MyResult.Success(true)
        else
            MyResult.Failure(Exception("Contraseña Incorrecta"))
    }
}