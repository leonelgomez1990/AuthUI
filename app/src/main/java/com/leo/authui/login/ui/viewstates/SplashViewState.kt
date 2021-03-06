package com.leo.authui.login.ui.viewstates

sealed class SplashViewState {
    object Idle : SplashViewState()
    object Loading : SplashViewState()
    object Outdated : SplashViewState()
    data class Failure(val exception: Exception) : SplashViewState()
}
