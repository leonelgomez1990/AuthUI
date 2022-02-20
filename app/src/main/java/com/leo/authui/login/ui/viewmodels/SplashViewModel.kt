package com.leo.authui.login.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leo.authui.core.utils.SingleLiveEvent
import com.leo.authui.login.ui.navigatorstates.SplashNavigatorStates
import com.leo.authui.login.ui.viewstates.SplashViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
): ViewModel() {

    private val _viewState: MutableLiveData<SplashViewState> = MutableLiveData()
    val viewState: LiveData<SplashViewState> get() = _viewState

    private val _navigation = SingleLiveEvent<SplashNavigatorStates>()
    val navigation: LiveData<SplashNavigatorStates> get() = _navigation

    init {
        _viewState.value = SplashViewState.Loading
        _viewState.value = SplashViewState.Idle
    }

    fun goToSignIn( delayMs: Long) {
        viewModelScope.launch {
            delay(delayMs)
            _navigation.value = SplashNavigatorStates.ToSignIn
        }
    }

}