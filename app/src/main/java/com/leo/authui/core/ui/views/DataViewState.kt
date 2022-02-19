package com.leo.authui.core.ui.views

sealed class DataViewState {
    object Ready: DataViewState()
    object Loading: DataViewState()
    object Refreshing: DataViewState()
    data class Failure(val exception: Exception): DataViewState()
    data class Alert(val title: Int, val message: Int): DataViewState()
}
