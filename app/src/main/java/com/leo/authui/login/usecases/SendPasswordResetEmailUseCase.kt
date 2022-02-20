package com.leo.authui.login.usecases

import com.leo.authui.login.data.UsersRepository

class SendPasswordResetEmailUseCase(private val usersRepository: UsersRepository) {
    suspend operator fun invoke(email: String) =
        usersRepository.sendPasswordResetEmail(email)
}