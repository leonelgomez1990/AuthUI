package com.leo.authui.login.usecases

import com.leo.authui.login.data.UsersRepository

class LoginWithEmailAndPasswordUseCase  (val usersRepository: UsersRepository) {
    suspend operator fun invoke(email: String, password: String) = usersRepository.loginWithEmailAndPassword(email, password)
}