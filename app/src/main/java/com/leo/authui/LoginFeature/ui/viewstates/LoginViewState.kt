package com.leo.authui.LoginFeature.ui.viewstates

sealed class LoginViewState{
    object Ready: LoginViewState()
    object Loading: LoginViewState()
    data class Done(val message: String): LoginViewState()
    data class Failure(val exception: Exception): LoginViewState()
    data class Alert(val title: Int, val message: Int): LoginViewState()
}

