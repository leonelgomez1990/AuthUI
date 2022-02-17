package com.leo.authui.login.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leo.authui.core.utils.MyResult
import com.leo.authui.core.utils.SingleLiveEvent
import com.leo.authui.login.ui.navigatorstates.SignInNavigatorStates
import com.leo.authui.login.ui.viewstates.LoginViewState
import com.leo.authui.login.usecases.LoginWithEmailAndPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    val loginWithEmailAndPasswordUseCase: LoginWithEmailAndPasswordUseCase
): ViewModel() {

    private val _navigation = SingleLiveEvent<SignInNavigatorStates>()
    val navigation: LiveData<SignInNavigatorStates> get() = _navigation

    private val _viewState: MutableLiveData<LoginViewState> = MutableLiveData()
    val viewState: LiveData<LoginViewState> get() = _viewState

    init {
        _viewState.value = LoginViewState.Ready
    }

    fun doUserLogin(email: String, password: String) {
        viewModelScope.launch {
            _viewState.value = LoginViewState.Loading
            when(val result = loginWithEmailAndPasswordUseCase(email, password)) {
                is MyResult.Failure -> { _viewState.value = LoginViewState.Failure(result.exception) }
                is MyResult.Success -> {
                    _viewState.value = LoginViewState.Done("Entramos")
                    goToMenuApplication()
                }
            }
        }
    }

    private fun goToMenuApplication() {
        _navigation.value = SignInNavigatorStates.ToMenuFeature
    }
}