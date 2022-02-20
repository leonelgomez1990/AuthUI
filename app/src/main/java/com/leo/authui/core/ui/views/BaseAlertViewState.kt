package com.leo.authui.core.ui.views

sealed class BaseAlertViewState {
    object Ready: BaseAlertViewState()
    object Loading: BaseAlertViewState()
    data class Failure(val exception: Exception): BaseAlertViewState()
    data class Alert(val title: Int, val message: Int): BaseAlertViewState()
}
