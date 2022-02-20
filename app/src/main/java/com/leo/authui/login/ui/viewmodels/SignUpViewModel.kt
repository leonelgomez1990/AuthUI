package com.leo.authui.login.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leo.authui.core.ui.views.BaseViewState
import com.leo.authui.core.utils.MyResult
import com.leo.authui.core.utils.SingleLiveEvent
import com.leo.authui.login.ui.navigatorstates.SignUpNavigatorStates
import com.leo.authui.login.usecases.CreateUserWithEmailAndPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    val createUserWithEmailAndPasswordUseCase: CreateUserWithEmailAndPasswordUseCase
): ViewModel() {

    private val _navigation = SingleLiveEvent<SignUpNavigatorStates>()
    val navigation : LiveData<SignUpNavigatorStates> get() = _navigation

    private val _viewState : MutableLiveData<BaseViewState> = MutableLiveData()
    val viewState : LiveData<BaseViewState> get() = _viewState

    init {
        _viewState.value = BaseViewState.Ready
    }

    fun doCreateNewUser(email: String, password: String) {
        viewModelScope.launch {
            _viewState.value = BaseViewState.Loading

            when(val result = createUserWithEmailAndPasswordUseCase(email, password)) {
                is MyResult.Success -> {
                    _viewState.value = BaseViewState.Ready
                    goToSignIn()
                }
                is MyResult.Failure -> {
                    _viewState.value = BaseViewState.Failure(result.exception)
                }
            }
        }
    }

    private fun goToSignIn() {
        _navigation.value = SignUpNavigatorStates.ToSignIn
    }

    fun goBack() {
        _navigation.value = SignUpNavigatorStates.ToGoBack
    }
}